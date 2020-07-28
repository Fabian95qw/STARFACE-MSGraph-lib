
# STARFACE-MSGraph-lib 
Moduleblocks to use the MS Graph REST-API (O365)

# Available Moduleblocks:
- CreateO365Provider -> Creates a Providerobject, that is required for all other Moduleblocks
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
