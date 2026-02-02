$ErrorActionPreference = "Stop"
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession

# 1. Login
Write-Host "[*] Logging in..."
$loginPage = Invoke-WebRequest -Uri "http://localhost:8080/login" -SessionVariable session -Method Get -UseBasicParsing
$csrf = $null
if ($loginPage.Content -match 'name="_csrf" value="([^"]+)"') { $csrf = $matches[1] }

$body = @{ email = "admin@example.com"; password = "password"; _csrf = $csrf }
$loginResp = Invoke-WebRequest -Uri "http://localhost:8080/login" -WebSession $session -Method Post -Body $body -MaximumRedirection 5 -UseBasicParsing

# 2. Check Dashboard for Name
Write-Host "[*] Checking Dashboard for 'Admin User' name..."
$dashboard = Invoke-WebRequest -Uri "http://localhost:8080/dashboard" -WebSession $session -Method Get -UseBasicParsing

if ($dashboard.Content -match "Admin User") {
    Write-Host "[+] SUCCESS: Found 'Admin User' on dashboard."
} else {
    Write-Host "[-] FAIL: 'Admin User' not found on dashboard."
}

if ($dashboard.Content -match "contact") {
    Write-Host "[+] SUCCESS: Found 'contact' link on dashboard."
} else {
    Write-Host "[-] FAIL: 'contact' link not found."
}

# 3. Check Contact Page
Write-Host "[*] Checking Contact Page..."
$contact = Invoke-WebRequest -Uri "http://localhost:8080/contact" -WebSession $session -Method Get -UseBasicParsing

if ($contact.StatusCode -eq 200) {
    Write-Host "[+] SUCCESS: Contact page loaded (200 OK)."
}

if ($contact.Content -match "Get in Touch") {
    Write-Host "[+] SUCCESS: Found 'Get in Touch' text."
} else {
    Write-Host "[-] FAIL: Content mismatch on contact page."
}

if ($contact.Content -match "fa-instagram") {
    Write-Host "[+] SUCCESS: Found Instagram icon."
} else {
    Write-Host "[-] FAIL: Instagram icon not found."
}

if ($contact.Content -match 'id="logo-link"') {
    Write-Host "[+] SUCCESS: Found 'logo-link' ID."
} else {
    Write-Host "[-] FAIL: logo-link ID not found."
}
