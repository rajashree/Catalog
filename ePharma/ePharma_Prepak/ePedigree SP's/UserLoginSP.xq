tig:create-stored-procedure("checkUserValidityBySessionID","
declare variable $sessionID as string external;
for $b in collection('tig:///EAGRFID/SysSessions') where $b/session[sessionid=$sessionID]
return 
(fn:convert-to-milliseconds( current-dateTime() ) -  fn:convert-to-milliseconds(adjust-dateTime-to-timezone($b/session/lastuse ) ) ) div ( 60 * 1000 )"),
tig:create-stored-procedure('updateLastUseInLoggin',"
declare variable $sesId as string external;
declare variable $luse as string external;

update 
for $d in collection('tig:///EAGRFID/SysSessions')/session[sessionid=$sesId]
replace value of $d/lastuse with $luse
"),
tig:create-stored-procedure("loginUserInfo","
declare variable $userName as string external;
 for $b in collection('tig:///EAGRFID/SysUsers')/User[UserName=$userName] 
 return 
 <User>
 {$b/UserID}
 {$b/FirstName}
 {$b/LastName}
 {$b/AccessLevel}
 {$b/BelongsToCompany}
 {$b/Password}
 {$b/Disable}
 {for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner where $i/name = $b/BelongsToCompany return $i/businessId} 
 </User> "),
tig:create-stored-procedure("checkUserValidityByID","
declare variable $userID as string external;
for $b in collection('tig:///EAGRFID/SysSessions') where $b/session[userid=$userID]
return 
(fn:convert-to-milliseconds( current-dateTime() ) -  fn:convert-to-milliseconds(adjust-dateTime-to-timezone($b/session/lastuse ) ) ) div ( 60 * 1000 )"),
tig:create-stored-procedure("deleteInvlidUserByID","
declare variable $userID as string external;
for $b in collection('tig:///EAGRFID/SysSessions') where $b/session[userid=$userID]
return 
tig:delete-document(document-uri( $b ))"),
tig:create-stored-procedure("deleteInvlidSessionID","
declare variable $sessionID as string external;
for $b in collection('tig:///EAGRFID/SysSessions') where $b/session[sessionid=$sessionID]
return 
tig:delete-document(document-uri( $b ))"),
tig:create-stored-procedure("deleteSessions","
for $b in collection('tig:///EAGRFID/SysSessions')
return 
tig:delete-document(document-uri( $b ))"),
tig:create-stored-procedure("CheckSessionExisting","
declare variable $sessionID as string external;
for $b in collection('tig:///EAGRFID/SysSessions') where $b/session[sessionid=$sessionID]
return 
$b")
