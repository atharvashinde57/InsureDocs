$ErrorActionPreference = "Stop"
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession

# 1. Login
Write-Host "[*] Attempting login..."
$loginPage = Invoke-WebRequest -Uri "http://localhost:8080/login" -SessionVariable session -Method Get -UseBasicParsing

# Extract CSRF
$csrf = $null
if ($loginPage.Content -match 'name="_csrf" value="([^"]+)"') {
    $csrf = $matches[1]
    Write-Host "[*] Found Login CSRF: $csrf"
}

$body = @{
    email = "admin@example.com"
    password = "password"
    _csrf = $csrf
}

try {
    $loginResp = Invoke-WebRequest -Uri "http://localhost:8080/login" -WebSession $session -Method Post -Body $body -MaximumRedirection 5 -UseBasicParsing
} catch {
    # 302 redirect is often caught as error in older PS, but -MaximumRedirection usually handles it
    Write-Host "[-] Login Request resulted in $($_.Exception.Message)"
}

# 2. Get Dashboard to get Upload CSRF
Write-Host "[*] Getting Dashboard..."
$dashboard = Invoke-WebRequest -Uri "http://localhost:8080/dashboard" -WebSession $session -Method Get -UseBasicParsing

if ($dashboard.Content -match 'name="_csrf" value="([^"]+)"') {
    $uploadCsrf = $matches[1]
    Write-Host "[*] Found Upload CSRF: $uploadCsrf"
} else {
    Write-Error "Could not find CSRF token on dashboard"
}

# 3. Upload File
Write-Host "[*] Uploading File..."
$dummyPdfPath = "test_upload.pdf"
Set-Content -Path $dummyPdfPath -Value "%PDF-1.4 dummy content"

# Boundary for multipart
$boundary = "---------------------------" + [Guid]::NewGuid().ToString().Replace("-", "")
$LF = "`r`n"

# Construct Body
$bodyLines = @(
    "--$boundary",
    "Content-Disposition: form-data; name=`"_csrf`"",
    "",
    "$uploadCsrf",
    "--$boundary",
    "Content-Disposition: form-data; name=`"title`"",
    "",
    "PowerShell Test Upload",
    "--$boundary",
    "Content-Disposition: form-data; name=`"policyNumber`"",
    "",
    "PS-123",
    "--$boundary",
    "Content-Disposition: form-data; name=`"file`"; filename=`"test_upload.pdf`"",
    "Content-Type: application/pdf",
    "",
    "$(Get-Content $dummyPdfPath -Raw)",
    "--$boundary--"
) -join $LF

try {
    $uploadResp = Invoke-WebRequest -Uri "http://localhost:8080/upload" `
        -WebSession $session `
        -Method Post `
        -ContentType "multipart/form-data; boundary=$boundary" `
        -Body $bodyLines `
        -UseBasicParsing
        
    if ($uploadResp.StatusCode -eq 200) {
        Write-Host "[+] Upload Request Completed with 200 OK"
        if ($uploadResp.Content -match "File uploaded successfully") {
            Write-Host "[+] SUCCESS: 'File uploaded successfully' message found on page!"
        } else {
            Write-Host "[?] Page loaded but specific success text not found. Check dashboard manually."
        }
    }
} catch {
    Write-Host "[-] Upload Failed: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    }
}
