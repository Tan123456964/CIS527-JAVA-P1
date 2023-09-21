### GROUP INFO
```bash
Group: 30
Team members: Tapon Das and Patrick Imoh 
```

### HELPFUL INFORMATION

#### Connect to Vpn:
```bash
umvpn.umd.umich.edu
```

#### connect to server:
```bash
$ssh username@login.umd.umich.edu -p 22 
# User your umich password 
# Verfiy with Duo app 
```

#### copy file to server:
```bash
scp -rv source -P 22 username@login.umd.umich.edu: destination-path
# User your umich password 
# Verfiy with Duo app 
```

#### run code with makefile:
```bash
$make Client.class      # Creates client class file  
$make Server.class      # Creates server class file 
```

#### Execute java code:
```bash
$java Client IP  #e.g., $ java Client 127.0.0.1
$java Server  
```



