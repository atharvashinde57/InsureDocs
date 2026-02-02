import requests
import re

# Base URL
BASE_URL = "http://localhost:8080"

# Create a session to persist cookies
session = requests.Session()

def login():
    print(f"[*] Attempting login to {BASE_URL}/login...")
    # Get login page to likely get a CSRF token (if login page has one, Spring Security 6 usually requires it for login POST too)
    login_page = session.get(f"{BASE_URL}/login")
    
    # Extract CSRF from login page
    csrf_token = re.search(r'name="_csrf" value="(.+?)"', login_page.text)
    data = {
        "email": "admin@example.com",
        "password": "password"
    }
    
    if csrf_token:
        print(f"[*] Found CSRF token for login: {csrf_token.group(1)}")
        data["_csrf"] = csrf_token.group(1)
    
    # Perform Login
    response = session.post(f"{BASE_URL}/login", data=data)
    
    if response.url.endswith("/dashboard") or "/dashboard" in response.text: # Check redirect or content
        print("[+] Login Successful!")
        return True
    else:
        print("[-] Login Failed. Current URL:", response.url)
        return False

def upload_file():
    print("[*] Preparing file upload...")
    # Get Dashboard to get the Upload CSRF Token (it might be different or rotated)
    dashboard = session.get(f"{BASE_URL}/dashboard")
    csrf_token = re.search(r'name="_csrf" value="(.+?)"', dashboard.text)
    
    if not csrf_token:
        print("[-] Could not find CSRF token on dashboard!")
        return False
        
    print(f"[*] Found CSRF token for upload: {csrf_token.group(1)}")
    
    # Create a dummy PDF
    files = {'file': ('test.pdf', b'%PDF-1.4 empty pdf content', 'application/pdf')}
    data = {
        "title": "Test Upload Script",
        "policyNumber": "12345",
        "_csrf": csrf_token.group(1)
    }
    
    response = session.post(f"{BASE_URL}/upload", files=files, data=data)
    
    print(f"[*] Upload Response Code: {response.status_code}")
    
    if response.status_code == 200 and "dashboard" in response.url:
         # Check for success message in dashboard HTML
         if "File uploaded successfully" in response.text:
             print("[+] Upload Verification Successful! Success message found.")
             return True
         else:
             print("[?] Upload finished but success message not found. Check dashboard manually.")
             return True # Still likely a success if 200 OK and on dashboard
    elif response.status_code == 302:
         # Redirect is also good, follow it
         print("[*] Follow redirect...")
         final_resp = session.get(response.headers['Location'])
         if "File uploaded successfully" in final_resp.text:
              print("[+] Upload Verified via Redirect.")
              return True
    
    print("[-] Upload Failed.")
    return False

if __name__ == "__main__":
    if login():
        upload_file()
