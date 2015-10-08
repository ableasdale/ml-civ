xquery version "1.0-ml";

 declare variable $doc := 
    xdmp:document-get("C:\Program Files (x86)\Steam\steamapps\common\Sid Meier's Civilization V\Assets\Gameplay\XML\Buildings\CIV5Buildings.xml",
     <options xmlns="xdmp:document-get">
        <format>xml</format>
        <repair>full</repair>
     </options>); 


declare function local:get-element-values($row as element(Row), $element-names as xs:string*) as element(td)* {
    for $j in $element-names 
    return
    for $i in $row/*
    (: where some fn:local-name($i) in $i satisfies ($i eq $j) :)
    where fn:local-name($i) eq $j
    (: (fn:local-name($i) satisfies $element-names) :)
    return element td{ text{ fn:data($i) } }
};

(: 
some $item in $sequence satisfies ($element eq $item)
:)
(:
if(empty($i/$element-name))
    then(element td {"-"})
    else(element td {fn:data($i/$element-name)}) :)


(: $doc/GameData/Table :)


 
(for $i in $doc/GameData/Buildings/Row
return element tr {
    local:get-element-values($i, ("Type", "Cost", "GoldMaintenance", "PrereqTech", "HurryCostModifier", "Culture"))
},

$doc/GameData/Buildings/Row)


 