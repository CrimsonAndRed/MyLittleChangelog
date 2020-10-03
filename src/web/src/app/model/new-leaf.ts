export interface NewLeaf {
    name: string,
    valueType: number,
    value: string,
}

export interface NewLeafWithId extends NewLeaf {
    id: number,
    vid: number,
}