import { GroupContent } from './group-content'
import { LeafContent } from './leaf-content'

export interface PastRadioEvent {
  value: GroupContent | LeafContent,
  parentId: number
  kind: "leaf" | "group"
}
