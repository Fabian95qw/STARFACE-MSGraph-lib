
# STARFACE-MSGraph-lib 
Moduleblocks to use the MS Graph REST-API (O365)

# Available Moduleblocks:
- CreateO365Provider (App-Secret)-> Creates a Providerobject, is required for any requests. Uses Applicationpermissions
- CreateO365Provider (DeviceCode)-> Creates a Providerobject, is required for any requests. Uses User Delegated Permissions
- CreateCodeFlowProvider -> Creates a OAuth2 request using a DeviceCode, so the user that is being used for the Delegated Permissions can login. Returns a Message with the Steps the user has to do to authenticate, and returns the raw DeviceCodeFlowProvider, for the CreateO365Provider (DeviceCode)
- GetUsers -> Returns all Users as a List<Map<String, Object>> 
- GetContactFoldersforUser -> Returns all ContactFolders for a User (List<Map<String,Object>>)
- GetContactsforUser -> Returns all Contacts for a User from a folder (List<Map<String,Object>>)
- GenericRequests -> Used for all kind of MSGraph REST-API requests. Can return a raw JSON-String, a List<Map<String,Object>> or a Map<String,Object> 

# How to use the Moduleblocks
Under https://github.com/Fabian95qw/STARFACE-MSGraph-lib/tree/master/module you'll find the Library, and an example Module. 
The library provides you with the building blocks for your own modules.

You'll find them in the Public Section in Expert Mode

  ![Moduleblocks](/img/moduleblocks.png "Moduleblocks")
  
# Using Moduleblocks with Applicationpermissions (Clientsecret)
Please look at this guide for creating an App with a Clientsecret: https://github.com/Fabian95qw/STARFACE-MSGraph-lib/blob/master/CreateApp-ClientSecret.md

# Using Moduleblocks with Delegated Userpermissions (DeviceCode)
Please look at this guide for creating an App with User Delegated rights https://github.com/Fabian95qw/STARFACE-MSGraph-lib/blob/master/CreateApp-DeviceCode.md
