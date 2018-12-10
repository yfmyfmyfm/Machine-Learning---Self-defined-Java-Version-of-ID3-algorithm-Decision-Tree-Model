	import java.util.*;

	public class DecisionTreeNode {
		private String root;
		private DecisionTreeNode parent;
		private List<String> branch;
		private List<DecisionTreeNode> children;
		private String np;
		
		/** Constructs a TreeNode with employee and supervisorNode. */
		public DecisionTreeNode (String root, DecisionTreeNode parent) {
			this.root = root;
			parent = null;
			branch = new ArrayList<String>();
			children = new ArrayList<DecisionTreeNode>();
			np = null;
		}
		
		/** Return the name in this node */
		public String getNode() {
			return root;
		}
		
		/** Return the parent for this node */
		public DecisionTreeNode getParent()	{
			return parent;
		}
		
		/** Return the children list for this node */
		public List<DecisionTreeNode> getChildren() {
			return children;
		}
		
		public String getnp() {
			return np;
		}
		
		public List<String> getBranch() {
			return branch;
		}
		
		public String updateRootName(String s) {
			root = s;
			return root;
		}
		
		public void addBranch(String b) {
			branch.add(b);
		}
		
		public DecisionTreeNode updataParent(DecisionTreeNode s) {
			parent = s;
			return parent;
		}
		
		public String updatenp(String s) {
			np = s;
			return np;
		}
		
		/** Add new child to this node */
		public void addChildren(DecisionTreeNode childrenNode) {
			children.add(childrenNode);
		}
		

	}