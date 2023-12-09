# CoReScreen                                                                                         
A powerful & collection of required tools to ss someone                                                                                   
This is a 1.8.8-1.16(un-tested) minecraft spigot/paper screen share plugin...                                                                                   
In this plugin all of required tools to profile & check someone for cheats has implemented                                                                     
                                                                                                  
# Features
 * Vanish
   * Fully hides players on packet level which is much more secure and cant be broken by other plugins
   * You can limit what vanished players can do based on permissions
   * A huge configuration to set messages & actions
 * Freeze
   * Create a log file of actions was does in ss (**SOON**)
   * Actions base on player's quit while frozen, or they time finish ( Clickable text )
   * Auto timer that count for you per player
   * Automatically block frozen player's messages ( Only show up for staffs )
   * Command whitelist that prevent player from using un registered commands while frozen
   * A huge configuration to set messages & actions
 * Profiler
   * Easy to use & non memory leak system ( async )
   * Profile players data based on packets & events
   * A huge configuration to set messages
 * Test
   * Fully automatic testing with a simple command
   * Show the result of test after testing finished
   * A huge configuration to set messages
‌‌‌                                                                                                                                                                                                                                                         
# Options
- Freeze: Let you freeze players for ss                                               
- Vanish: Let invisible to normal players                                     
- Profiler: Let you scan player's actions by items                                                    
- Test: Let you check something in player's client by acting to them                                            
- OperationBlock(**SOON**): Let you block options of player                                     
                          
## Commands
### Freeze:                                                                                                                                                                                                                         
  * usage: /freeze {player-name}
  * aliases: /f, /freeze
                                                                                                                                                                          
### Vanish:                                                                                    
  * usage: /vanish [player-name]
  * aliases: /v, /vanish
                                                                                                                                                                      
### Profiler:                                                                                   
  * usage: /profiler {player-name} {option-to-profile}                                                                                   
  * aliases: /p, /profiler
                                                                                                                                                                      
### Test:                                                                                    
  * usage: /test {player-name} {option-to-test}                                                                                   
  * alias: /t, /test                                                                                   
                                                                                                                                                                    
## Permissions
### Freeze:                                                                                                                                                                                                                                                  
  * main: `corescreen.freeze`                                                                                                                                        
  * alerts: `corescreen.freeze.alert`
  * command-whitelist-bypass: `corescreen.freeze.commandbypass`
  * chat-messages: `corescreen.freeze.chat` (Frozen players have a private chat with staffs have this permnission)
                                                                                                                                                                                                                     
### Vanish:                                                                                                                                                                       
  * main-self: `corescreen.vanish.self`
  * main-other: `corescreen.vanish.other`
  * permission-blockplace: `corescreen.permission.blockplace`                                                                                                                          
  * permission-blockbreak: `corescreen.permission.blockbreak`                                                                                                                         
  * permission-interact-entity: `corescreen.permission.interactentity`                                                                                                                                
  * permission-interact-block: `corescreen.permission.interactblock`                                                                                                                              
  * permission-consume: `corescreen.permission.consume`                                                                                                                  
  * permission-damage-other: `corescreen.permission.damageother`                                                                                                                                
  * permission-damage-self: `corescreen.permission.damageself`                                                                                                                                 
  * permission-bow-use: `corescreen.permission.usebow`
                                                                                                                                                                                                                                                         
### Profiler:                                                                                                                                                                       
  * append: `corescreen.profiler.append`
  * remove: `corescreen.profiler.remove`
                                                                                                                                                                      
### Test:                                                                                                                                                                       
  * main: `corescreen.test`                                                                                                                                                                 
  * modules-perm: `corescreen.test.{module}`



# Placeholders                                                                                                   
  * PREFIX: `crs`                                                                                                                                     
  * `onlineplayers` return a int that's online players amount  ( excludes vanished players )                                                                                   
  * `vanishplayers` return a int that's vanished players amount                                                                                   
  * `isvanish` return `true` if player is vanished else `false`                                                                                   
Example: `crs_onlineplayers` `crs_isvanish`


# ***Leave star if you like this plugin***