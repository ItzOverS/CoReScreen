# CoReScreen
A powerful & collection of required tools to ss someone

 ‌‌‌
# Options
Freeze: Let you freeze players for ss ( options in config.yml )                                                   
Vanish: Let invisible to normal players ( options in config.yml )                                      
Profiler: Let you scan player's actions by items ( options in config.yml )                                                       
Test: Let you check something in player's client by acting to them ( options in config.yml )                                             
OperationBlock(**SOON**): Let you block options of player                                     
                          
# Commands
Freeze:                                                                                                                                                                                                                         
  * usage: /freeze {player-name}
  * aliases: /f, /freeze
                                                                                                                                                                          
Vanish:                                                                                    
  * usage: /vanish [player-name]
  * aliases: /v, /vanish
                                                                                                                                                                      
Profiler:                                                                                   
  * usage: /profiler {player-name} {option-to-profile}                                                                                   
  * aliases: /p, /profiler
                                                                                                                                                                      
Test:                                                                                    
  * usage: /test {player-name} {option-to-test}                                                                                   
  * alias: /t, /test                                                                                   
                                                                                                                                                                    
# Permissions
Freeze:                                                                                                                                                                                                                                                  
  * main: corescreen.freeze                                                                                                                                               
  * alerts: corescreen.freeze.alert 
  * chat-messages: corescreen.freeze.chat (Frozen players have a private chat with staffs have this permnission)
                                                                                                                                                                                                                     
Vanish:                                                                                                                                                                       
  * main-self: corescreen.vanish.self
  * main-other: corescreen.vanish.other
  * permission-blockplace: corescreen.permission.blockplace                                                                                                                                  
  * permission-blockbreak: corescreen.permission.blockbreak                                                                                                                                  
  * permission-interact-entity: corescreen.permission.interactentity                                                                                                                                  
  * permission-interact-block: corescreen.permission.interactblock                                                                                                                                  
  * permission-consume: corescreen.permission.consume                                                                                                                                  
  * permission-damage-other: corescreen.permission.damageother                                                                                                                                  
  * permission-damage-self: corescreen.permission.damageself                                                                                                                                  
  * permission-bow-use: corescreen.permission.usebow
                                                                                                                                                                                                                                                         
Profiler:                                                                                                                                                                       
  * append: corescreen.profiler.append
  * remove: corescreen.profiler.remove
                                                                                                                                                                      
Test:                                                                                                                                                                       
  * main: corescreen.test                                                                                                                                                                      
  * **SOON** | modules-perm: corescreen.test.{module}
