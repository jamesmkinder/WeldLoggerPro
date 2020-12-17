WeldLoggerPro v1.0
James Kinder

Installation and configuration instructions:
1. Download all files in this repository.  DO NOT change any of the file hierarchies in the package.
2. To start the application, run the .exe file in WeldLoggerApp/out/production/WeldLoggerApp/Model/
3. This application depends on and interacts with the MS Access database found in this package named WeldLog.accdb. The application finds this database using the filepath found in WeldLoggerApp/out/production/WeldLoggerApp/WeldLogDatabasePath.txt.  If you want to move the Access database to a different location, you MUST also update the .txt file to contain the updated absolute filepath of the database.  The filepath should begin with two forward slashes and the drive letter, and you must use forward slashes in the filepath. For example, "//C:/Users/James/Desktop/WeldLog.accdb" is an appropriate filepath, whereas "C:/Users/James/Desktop/WeldLog.accdb" is not.
4. If you want to access the .exe file somewhere other than from within the WeldLoggerApp package, you must create a shortcut.  The application uses relative filepaths to find the .txt file and access the database, if it moves, then it will not be able to find the .txt file.  You may also pin the application to the taskbar by right-clicking the .exe file and selecting "Pin to taskbar".
