# Netlogger android lib
- to use:
 # add this to project:
 
implementation("com.github.koai-dev:base:$lastVersion") (min is v1.9.1)

implementation("com.github.koai-dev:netlogger:$lastVersion") (min is v1.1.2)

- to get lastVersion: copy repo link and paste to jitpack.io
# in YourApplication:
add this:
![image](https://github.com/user-attachments/assets/887b460e-ce46-4b15-8105-56ce9d2f1cad)
# in service:
create apiClient:
![image](https://github.com/user-attachments/assets/6355843d-36f3-407f-ac78-afcafcbe108d)
init apiClient in service module like this:
![image](https://github.com/user-attachments/assets/8ae1f22c-0c69-441d-81fa-32ce3f59b81b)
# in your activity or fragment:
to open to netlog screen:
call this:
![image](https://github.com/user-attachments/assets/4a1730b4-8b02-4d8a-ad52-402e889de080)
you can handle by click event or implement shake event
--->  # check in sample project for final guide version


