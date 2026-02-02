$ErrorActionPreference = "Stop"
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession

# 1. Login (Just to be safe, though /policies is public now)
Write-Host "[*] Logging in..."
try {
    $loginPage = Invoke-WebRequest -Uri "http://localhost:8080/login" -SessionVariable session -Method Get -UseBasicParsing
    $csrf = $null
    if ($loginPage.Content -match 'name="_csrf" value="([^"]+)"') { $csrf = $matches[1] }
    $body = @{ email = "admin@example.com"; password = "password"; _csrf = $csrf }
    $loginResp = Invoke-WebRequest -Uri "http://localhost:8080/login" -WebSession $session -Method Post -Body $body -MaximumRedirection 5 -UseBasicParsing
    Write-Host "[+] Logged in."
}
catch {
    Write-Host "[-] Login failed or already logged in (proceeding...)"
}

# 2. Check Policies Page (All Users)
Write-Host "[*] Checking /policies content..."
$policies = Invoke-WebRequest -Uri "http://localhost:8080/policies" -WebSession $session -Method Get -UseBasicParsing

if ($policies.StatusCode -eq 200) {
    Write-Host "[+] SUCCESS: Page loaded (200 OK)."
}

if ($policies.Content -match "Policy Holders") {
    Write-Host "[+] SUCCESS: Found Header 'Policy Holders'."
}
else {
    Write-Host "[-] FAIL: Header missing."
}

if ($policies.Content -match "Admin User") {
    Write-Host "[+] SUCCESS: Found 'Admin User' in listing."
}
else {
    Write-Host "[-] FAIL: 'Admin User' not found in listing."
}

# 3. Check Search Functionality
Write-Host "[*] Checking Search Functionality..."
$searchRes = Invoke-WebRequest -Uri "http://localhost:8080/policies?search=Admin" -WebSession $session -Method Get -UseBasicParsing

if ($searchRes.Content -match "Admin User") {
    Write-Host "[+] SUCCESS: Search for 'Admin' returned result."
}
else {
    Write-Host "[-] FAIL: Search for 'Admin' failed."
}

$emptyRes = Invoke-WebRequest -Uri "http://localhost:8080/policies?search=Ghost" -WebSession $session -Method Get -UseBasicParsing
if ($emptyRes.Content -notmatch "Admin User") {
    Write-Host "[+] SUCCESS: Search for 'Ghost' correctly excluded Admin."
}
else {
    Write-Host "[-] FAIL: Search for 'Ghost' still showed Admin."
}
