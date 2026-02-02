$ErrorActionPreference = "Stop"
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession

# 1. Login
try {
    $loginPage = Invoke-WebRequest -Uri "http://localhost:8080/login" -SessionVariable session -Method Get -UseBasicParsing
    $csrf = $null
    if ($loginPage.Content -match 'name="_csrf" value="([^"]+)"') { $csrf = $matches[1] }
    $body = @{ email = "admin@example.com"; password = "password"; _csrf = $csrf }
    $loginResp = Invoke-WebRequest -Uri "http://localhost:8080/login" -WebSession $session -Method Post -Body $body -MaximumRedirection 5 -UseBasicParsing
    Write-Host "[+] Logged in."
}
catch {
    Write-Host "[-] Login failed or already logged in."
}

# 2. Search Positive (Admin)
$searchRes = Invoke-WebRequest -Uri "http://localhost:8080/policies?search=Admin" -WebSession $session -Method Get -UseBasicParsing
if ($searchRes.Content -match "Active Policies") {
    # Check if we have rows
    # Simple check: does it NOT say "No policy holders found"
    if ($searchRes.Content -notmatch "No policy holders found matching your search") {
        Write-Host "[+] SUCCESS: Search for 'Admin' returned results."
    }
    else {
        Write-Host "[-] FAIL: Search for 'Admin' showed empty state."
    }
}

# 3. Search Negative (Ghost)
$emptyRes = Invoke-WebRequest -Uri "http://localhost:8080/policies?search=Ghost" -WebSession $session -Method Get -UseBasicParsing
if ($emptyRes.Content -match "No policy holders found matching your search") {
    Write-Host "[+] SUCCESS: Search for 'Ghost' correctly showed empty state."
}
else {
    Write-Host "[-] FAIL: Search for 'Ghost' did NOT show empty state (found results)."
}
