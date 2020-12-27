export interface TreeNode<T> {
	parent: TreeNode<T>;
	children: TreeNode<T>[];
	value: T;
}