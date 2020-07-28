
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
<Placeholder Development_Moduleblocks>
  ![Moduleblocks](/img/moduleblocks.png "Moduleblocks")


# Creating an App for Office365 access for your STARFACE
## Creating the app
In order for the Moduleblocks to work on your O365 Platform, you need to supply it with a Tenant-ID, a Client-ID and a Client-Secret.

For this you need to login your Azure Portal (https://portal.azure.com)
And switch over to the Azure-Active-Directory ⇒ App registrations

There you create a new registration
  ![create_app](/img/create_app.png "create_app")
  
Once the App is created you need to save the Application (client) ID and the Directory (tenant) ID for later use.

  ![app_info](/img/app_info.png "app_info")
We also need a client secret, which you can create in the tab "Certificate & secrets"
  ![create_secret](/img/create_secret.png "create_secret")

Once the Secret is created, save it as well.

## Setting API-Permissions
Even tough we have valid credentials for our STARFACE PBX we still won't be able to access the Platform without giving proper permissions.

For this change to the tab "API permissions" inside of the STARFACE_App
There you can add all kind of permissions, depending on what you want to access from the STARFACE

**Important! You can only use Application Permissions**

I recommend the following permissions:
- Contacts.Read (Allows to Read contacts in all mailboxes)
- Users.Read.All (Allows to Read all user's full profiles)

Once the permissions are added, admin-consent has to be given by an administrator
  ![admin-consent](/img/admin-consent.png "admin-consent")
  
Now with the Permissions set we can actually use the Moduleblocks
  ![createblock](/img/createblock.png "createblock")

