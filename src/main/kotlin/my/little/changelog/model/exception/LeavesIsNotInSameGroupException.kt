package my.little.changelog.model.exception

import my.little.changelog.model.leaf.Leaf

class LeavesIsNotInSameGroupException(leaf1: Leaf, leaf2: Leaf) :
    RuntimeException("Could not modify leaves that is not in the same group. IDs [${leaf1.id.value}] [${leaf2.id.value}]")
