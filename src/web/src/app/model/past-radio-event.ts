import { PastGroupContent } from './group-content'
import { PastLeafContent } from './leaf-content'

export interface PastRadioEvent {
  value: PastGroupContent | PastLeafContent,
  parentId: number,
  kind: "leaf" | "group"
}
