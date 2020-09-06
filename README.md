Структура версионного объекта:
    version
    content

GroupContent: [{
       identifier: String,
       groupContent: [GroupContent],
       leafContent: [LeafContent]
}]

LeafContent: {
    identifier: String,
    valueType: ValueType,
    value: {}
}

ContentType:
    0 - Group
    1 - Leaf

ValueType: 
    0 - String,
    1 - Int,
    2 - Int[],
    3 - Sting[]

ver 1:
Герои - GroupContent
    Инвокер - GroupContent
        Санстрайк - GroupContent
            Урон: 400|500|600 - LeafContent
            Стоимость: 300    - LeafContent

fields: [
    "heroes":{name: "Герои"},
    "hero-invoker":{name: "Инвокер"},
    "hero-invoker-damage":{name: "Урон"}
]

meta: [{
    groupContent: [
        id: "heroes",
        groupContent: [
            id: "hero-invoker",
            groupContent: [
                leafContent: [
                    id: "hero-invoker-damage"
                ]
            ]
        ]
    ]
}]

-----------------------------------------------------------

GroupContent: [{
    id: 0,
    identifier: "Герои",
    groupContent: [{ 
        id: 1,
        identifier: "Инвокер",
        groupContent: [{
            id: 2,
            identifier: "Санстрайк",
            leafContent: [{
                id: 3,
                identifier: "Урон",
                valueType: 2,
                value: [400,500,600]
            }, {
                id: 4,
                identifier: "Стоимость",
                valueType: 1,
                value: 300
            }]
        }]
    }]
}]
            
ver 2:
Герои - GroupContent
    Инвокер - GroupContent
        Санстрайк-Урон: 400|600|700 - LeafContent
        Санстрайк-Стоимость: 300    - LeafContent
        
GroupContent: [{
    id: 0,
    identifier: "Герои",
    groupContent: [{
        id: 1,
        identifier: "Инвокер",
        leafContent: [{
            id: 3,
            identifier: "Урон",
            valueType: 2,
            value: [400,500,600]
        }, {
            id: 4,
            identifier: "Стоимость",
            valueType: 1,
            value: 300
        }]
    }]
}]

---------------------------------------------------------------------

Version: {
    version: 2
    fields: [
        "1":{
            name: "Герои"
        },
        "2":{
            name: "Инвокер"
        },
        "3":{
            name: "Санстрайк-Урон",
            value: [400,600,700]
        }
    ],
    meta: [{
        groupContent: [{
            id: 1,
            groupContent: [{
                id: 2,
                leafContent: [{id: 3}]
            }]
        }]
    }]
}




groupContent: [{
    id: 0,
    leafContent: {
        id: 1,
        value: 10
    }
}]

groupContent: [{
    id: 0,
    leafContent: {
        id: 1,
        value: 20
    }
}]

v1
leafContent: {
    id: 1,
    value: 10
}

v2
leafContent: {
    id: 1,
    value: 20
}

id: 1
 +10


----
TODO
- Не генерируется VID

----------
Запросы:

WITH RECURSIVE tmp_groups AS (
		SELECT * FROM groups 
		WHERE (vid in (SELECT group_vid FROM leaves where version_id = 2)) or 
				(vid in (SELECT vid FROM groups where version_id = 2))
	UNION
		SELECT g.* FROM groups AS g JOIN tmp_groups ON g.vid=tmp_groups.parent_vid
) SELECT * FROM tmp_groups;

SELECT grouped.* FROM (SELECT g.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS g) as grouped
WHERE grouped.version_id=grouped.max;