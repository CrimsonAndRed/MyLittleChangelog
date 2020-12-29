import { TreeNode } from "app/model/tree";

export function formatTree<T>(rootChildren: T[], childExtractor: (t: T) => T[]): TreeNode<T> {
    const root: TreeNode<T> = { parent: null, children: [], value: null };
    formatTreeNode(root, rootChildren, childExtractor)
    return root;
}

function formatTreeNode<T>(node: TreeNode<T>, children: T[], childExtractor: (t: T) => T[]) {
    node.children = children.map((c) => {
        const newNode: TreeNode<T> = { parent: node, children: [], value: c }
        formatTreeNode(newNode, childExtractor(c), childExtractor)
        return newNode;
    })
}