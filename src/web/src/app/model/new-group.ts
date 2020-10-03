export interface NewGroup {
    name: string;
    vid: number;
    parentId: number;
}

export interface NewGroupWithId extends NewGroup {
    id: number;
}