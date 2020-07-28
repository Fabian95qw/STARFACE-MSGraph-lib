# Creating an App for Office365 access for your STARFACE with User Delegated Rights (Devicecode)
## Creating the app
In order for the Moduleblocks to work on your O365 Platform, you need to supply it with a Tenant-ID, a Client-ID

For this you need to login your Azure Portal (https://portal.azure.com)
And switch over to the Azure-Active-Directory ⇒ App registrations

There you create a new registration
  ![create_app](/img/create_app.png "create_app")
  
Once the App is created you need to save the Application (client) ID and the Directory (tenant) ID for later use.

  ![app_info](/img/app_info.png "app_info")

## Setting API-Permissions
Even tough we have valid credentials for our STARFACE PBX we still won't be able to access the Platform without giving proper permissions.

For this change to the tab "API permissions" inside of the STARFACE_App
There you can add all kind of permissions, depending on what you want to access from the STARFACE

**Important! You can only use Delegated Permissions**

Once the permissions are added, admin-consent has to be given by an administrator
  ![admin-consent](/img/admin-consent.png "admin-consent")
  
Now with the Permissions set we can actually use the Moduleblocks
  ![createblock](/img/createblock.png "createblock")

