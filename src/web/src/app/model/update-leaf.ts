export interface LeafToUpdate {
  name: string,
  valueType: number,
  value: string,
  parentId: number
}

export interface UpdatedLeaf {
  id: number,
  vid: number,
  name: string,
  valueType: number,
  value: string,
}