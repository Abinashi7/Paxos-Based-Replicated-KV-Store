included:
All source files (.java)
this README
Makefile

Please copy all the files within each folder to corresponding server folder, 
and run the following command to run the java file. 

Project #4

Please run make in the terminal to compile all the java files. 
then type each line into a new terminal instance.
=====================================================================
||                 Node 1 ~ 5 // 172.22.71.27 ~ 31                 ||
=====================================================================

TERMINAL-------------------------------------------------
|                                                       |
| $  rmiregistry &                                      |
| $  make                                               |
| $  java KeyValueServer1                               |  
|                                                       |
---------------------------------------------------------
* replace 1 with 2~ 5 for other servers (i.e. for server 2, type "java KeyValueServer2")


=====================================================================
||                  Coordinator // 172.22.71.32                    ||
=====================================================================

TERMINAL-------------------------------------------------
|                                                       |
| $  rmiregistry &                                      |
| $  make                                               |
| $  java KeyValueCoordinator                           |  
|                                                       |
---------------------------------------------------------

=====================================================================
||                     Client // 172.22.71.33                      ||
=====================================================================

TERMINAL-------------------------------------------------
|                                                       |
| $  make                                               |
| $  java KeyValueClient                                |  
|                                                       |
---------------------------------------------------------
