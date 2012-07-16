package tree;

/*
 * http://neuhubs.blog.sohu.com/49464057.html
 * http://www.if-yu.info/2010/10/3/suffix-tree.html
 * http://blog.csdn.net/TsengYuen/article/details/4815921
 * http://blog.csdn.net/v_july_v/article/details/6897097
 * http://gaterking.blog.51cto.com/69893/340602
 * http://blog.csdn.net/ljsspace/article/details/6596509 里面有UKK算法 Ukkonen算法及其实现
 */
//里面有UKK算法 Ukkonen算法及其实现
public class SuffixTreeC
{

	/* Error return value for some functions. Initialized in ST_CreateTree. */
	int		ST_ERROR;

	/* Used for statistic measures of speed. */
	int		counter;
	/* Used for statistic measures of space. */
	int		heap;
	/*
	 * Used to mark the node that has no suffix link yet. By Ukkonen, it will
	 * have one by the end of the current phase.
	 */
	NODE	suffixless	= null;

	NODE create_node(NODE father, int start, int end, int position)
	{
		/* Allocate a node. */
		NODE node = new NODE();

		// heap+=sizeof(NODE);

		/*
		 * Initialize node fields. For detailed description of the fields see
		 * suffix_tree.h
		 */
		node.sons = null;
		node.right_sibling = null;
		node.left_sibling = null;
		node.suffix_link = null;
		node.father = father;
		node.path_position = position;
		node.edge_label_start = start;
		node.edge_label_end = end;
		return node;
	}

	/******************************************************************************/
	/*
	 * find_son : Finds son of node that starts with a certain character.
	 * 
	 * Input : the tree, the node to start searching from and the character to
	 * be searched in the sons.
	 * 
	 * Output: A pointer to the found son, 0 if no such son.
	 */

	NODE find_son(SUFFIX_TREE tree, NODE node, char character)
	{
		/* Point to the first son. */
		node = node.sons;
		/*
		 * scan all sons (all right siblings of the first son) for their first
		 * character (it has to match the character given as input to this
		 * function.
		 */
		while (node != null && tree.tree_string.charAt(node.edge_label_start) != character) {
			counter++;
			node = node.right_sibling;
		}
		return node;
	}

	/******************************************************************************/
	/*
	 * get_node_label_end : Returns the end index of the incoming edge to that
	 * node. This function is needed because for leaves the end index is not
	 * relevant, instead we must look at the variable "e" (the global virtual
	 * end of all leaves). Never refer directly to a leaf's end-index.
	 * 
	 * Input : the tree, the node its end index we need.
	 * 
	 * Output: The end index of that node (meaning the end index of the node's
	 * incoming edge).
	 */

	int get_node_label_end(SUFFIX_TREE tree, NODE node)
	{
		/* If it's a leaf - return e */
		if (node.sons == null)
			return tree.e;
		/* If it's not a leaf - return its real end */
		return node.edge_label_end;
	}

	/******************************************************************************/
	/*
	 * get_node_label_length : Returns the length of the incoming edge to that
	 * node. Uses get_node_label_end (see above).
	 * 
	 * Input : The tree and the node its length we need.
	 * 
	 * Output: the length of that node.
	 */

	int get_node_label_length(SUFFIX_TREE tree, NODE node)
	{
		/* Calculate and return the lentgh of the node */
		return get_node_label_end(tree, node) - node.edge_label_start + 1;
	}

	/******************************************************************************/
	/*
	 * is_last_char_in_edge : Returns 1 if edge_pos is the last position in
	 * node's incoming edge.
	 * 
	 * Input : The tree, the node to be checked and the position in its incoming
	 * edge.
	 * 
	 * Output: the length of that node.
	 */

	char is_last_char_in_edge(SUFFIX_TREE tree, NODE node, int edge_pos)
	{
		if (edge_pos == get_node_label_length(tree, node) - 1)
			return 1;
		return 0;
	}

	/******************************************************************************/
	/*
	 * connect_siblings : Connect right_sib as the right sibling of left_sib and
	 * vice versa.
	 * 
	 * Input : The two nodes to be connected.
	 * 
	 * Output: None.
	 */

	void connect_siblings(NODE left_sib, NODE right_sib)
	{
		/* Connect the right node as the right sibling of the left node */
		if (left_sib != null)
			left_sib.right_sibling = right_sib;
		/* Connect the left node as the left sibling of the right node */
		if (right_sib != null)
			right_sib.left_sibling = left_sib;
	}

	/******************************************************************************/
	/*
	 * apply_extension_rule_2 : Apply "extension rule 2" in 2 cases: 1. A new
	 * son (leaf 4) is added to a node that already has sons: (1) (1) / \ . / |
	 * \ (2) (3) (2)(3)(4)
	 * 
	 * 2. An edge is split and a new leaf (2) and an internal node (3) are
	 * added: | | | (3) | . / \ (1) (1) (2)
	 * 
	 * Input : See parameters.
	 * 
	 * Output: A pointer to the newly created leaf (new_son case) or internal
	 * node (split case).
	 */
	/**
	 * 
	 * @param node
	 *            :Node 1 (see drawings)
	 * @param edge_label_begin
	 *            :Start index of node 2's incoming edge
	 * @param edge_label_end
	 *            : End index of node 2's incoming edge
	 * @param path_pos
	 *            :Path start index of node 2
	 * @param edge_pos
	 *            :Position in node 1's incoming edge where split is to be
	 *            performed
	 * @param type
	 *            :Can be 'new_son' or 'split'
	 * @return
	 */
	NODE apply_extension_rule_2(NODE node, int edge_label_begin, int edge_label_end, int path_pos, int edge_pos, RULE_2_TYPE type)
	{
		NODE new_leaf, new_internal, son;
		/*-------new_son-------*/
		if (type == RULE_2_TYPE.new_son) {

			// for DEBUG
			// System.out.print("rule 2: new leaf ("+edge_label_begin+","+edge_label_end+")\n");

			/* Create a new leaf (4) with the characters of the extension */
			new_leaf = create_node(node, edge_label_begin, edge_label_end, path_pos);
			/* Connect new_leaf (4) as the new son of node (1) */
			son = node.sons;
			while (son.right_sibling != null)
				son = son.right_sibling;
			connect_siblings(son, new_leaf);
			/* return (4) */
			return new_leaf;
		}
		/*-------split-------*/
		// for DEBUG
		// System.out.print("rule 2: split ("+edge_label_begin+","+edge_label_end+")\n");
		/* Create a new internal node (3) at the split point */
		new_internal = create_node(node.father, node.edge_label_start, node.edge_label_start + edge_pos, node.path_position);
		/*
		 * Update the node (1) incoming edge starting index (it now starts where
		 * node (3) incoming edge ends)
		 */
		node.edge_label_start += edge_pos + 1;

		/* Create a new leaf (2) with the characters of the extension */
		new_leaf = create_node(new_internal, edge_label_begin, edge_label_end, path_pos);

		/* Connect new_internal (3) where node (1) was */
		/* Connect (3) with (1)'s left sibling */
		connect_siblings(node.left_sibling, new_internal);
		/* connect (3) with (1)'s right sibling */
		connect_siblings(new_internal, node.right_sibling);
		node.left_sibling = null;

		/* Connect (3) with (1)'s father */
		if (new_internal.father.sons == node)
			new_internal.father.sons = new_internal;

		/* Connect new_leaf (2) and node (1) as sons of new_internal (3) */
		new_internal.sons = node;
		node.father = new_internal;
		connect_siblings(node, new_leaf);
		/* return (3) */
		return new_internal;
	}

	/******************************************************************************/
	/*
	 * trace_single_edge : Traces for a string in a given node's OUTcoming edge.
	 * It searches only in the given edge and not other ones. Search stops when
	 * either whole string was found in the given edge, a part of the string was
	 * found but the edge ended (and the next edge must be searched too -
	 * performed by function trace_string) or one non-matching character was
	 * found.
	 * 
	 * Input : The string to be searched, given in indices of the main string.
	 * 
	 * Output: (by value) the node where tracing has stopped. (by reference) the
	 * edge position where last match occured, the string position where last
	 * match occured, number of characters found, a flag for signaling whether
	 * search is done, and a flag to signal whether search stopped at a last
	 * character of an edge.
	 */

	NODE trace_single_edge(SUFFIX_TREE tree,
	/* Node to start from */
	NODE node,
	/* String to trace */
	PATH str,
	/* Last matching position in edge */
	MyInteger edge_pos,
	/* Last matching position in tree source string */
	MyInteger chars_found,
	/* Skip or no_skip */
	SKIP_TYPE type,
	/* 1 if search is done, 0 if not */
	MyInteger search_done)
	{
		NODE cont_node;
		int length, str_len;

		/* Set default return values */
		search_done.intValue = 1;
		edge_pos.intValue = 0;

		/*
		 * Search for the first character of the string in the outcoming edge of
		 * node
		 */
		cont_node = find_son(tree, node, tree.tree_string.charAt(str.begin));
		if (cont_node == null) {
			/* Search is done, string not found */
			edge_pos.intValue = get_node_label_length(tree, node) - 1;
			chars_found.intValue = 0;
			return node;
		}

		/* Found first character - prepare for continuing the search */
		node = cont_node;
		length = get_node_label_length(tree, node);
		str_len = str.end - str.begin + 1;

		/* Compare edge length and string length. */
		/*
		 * If edge is shorter then the string being searched and skipping is
		 * enabled - skip edge
		 */
		if (type == SKIP_TYPE.skip) {
			if (length <= str_len) {
				(chars_found.intValue) = length;
				(edge_pos.intValue) = length - 1;
				if (length < str_len)
					search_done.intValue = 0;
			}
			else {
				(chars_found.intValue) = str_len;
				(edge_pos.intValue) = str_len - 1;
			}

			counter++;

			return node;
		}
		else {
			/* Find minimum out of edge length and string length, and scan it */
			if (str_len < length)
				length = str_len;

			for (edge_pos.intValue = 1, chars_found.intValue = 1; edge_pos.intValue < length; (chars_found.intValue)++, (edge_pos.intValue)++) {

				counter++;

				/*
				 * Compare current characters of the string and the edge. If
				 * equal - continue
				 */
				if (tree.tree_string.charAt(node.edge_label_start + edge_pos.intValue) != tree.tree_string.charAt(str.begin + edge_pos.intValue)) {
					(edge_pos.intValue)--;
					return node;
				}
			}
		}

		/* The loop has advanced edge_pos.intValue one too much */
		(edge_pos.intValue)--;

		if ((chars_found.intValue) < str_len)
			/* Search is not done yet */
			search_done.intValue = 0;

		return node;
	}

	/******************************************************************************/
	/*
	 * trace_string : Traces for a string in the tree. This function is used in
	 * construction process only, and not for after-construction search of
	 * substrings. It is tailored to enable skipping (when we know a suffix is
	 * in the tree (when following a suffix link) we can avoid comparing all
	 * symbols of the edge by skipping its length immediately and thus save
	 * atomic operations - see Ukkonen's algorithm, skip trick). This function,
	 * in contradiction to the function trace_single_edge, 'sees' the whole
	 * picture, meaning it searches a string in the whole tree and not just in a
	 * specific edge.
	 * 
	 * Input : The string, given in indice of the main string.
	 * 
	 * Output: (by value) the node where tracing has stopped. (by reference) the
	 * edge position where last match occured, the string position where last
	 * match occured, number of characters found, a flag for signaling whether
	 * search is done.
	 */
	/**
	 * 
	 * @param tree
	 * @param node
	 *            Node to start from
	 * @param str
	 *            String to trace
	 * @param edge_pos
	 *            Last matching position in edge
	 * @param chars_found
	 *            Last matching position in tree string
	 * @param type
	 *            skip or not
	 * @return
	 */
	NODE trace_string(SUFFIX_TREE tree, NODE node, PATH str, MyInteger edge_pos, MyInteger chars_found, SKIP_TYPE type)
	{
		/*
		 * This variable will be 1 when search is done. It is a return value
		 * from function trace_single_edge
		 */
		MyInteger search_done = new MyInteger(0);

		/*
		 * This variable will hold the number of matching characters found in
		 * the current edge. It is a return value from function
		 * trace_single_edge
		 */
		MyInteger edge_chars_found = new MyInteger(0);

		chars_found.intValue = 0;

		while (search_done.intValue == 0) {
			edge_pos.intValue = 0;
			edge_chars_found.intValue = 0;

			// str is changed in trace_single_edge? not changed. safe.
			// clone str
			PATH path = new PATH();
			path.begin = str.begin;
			path.end = str.end;

			node = trace_single_edge(tree, node, path, edge_pos, edge_chars_found, type, search_done);
			str.begin += edge_chars_found.intValue;
			chars_found.intValue += edge_chars_found.intValue;
		}
		return node;
	}

	/******************************************************************************/
	/*
	 * follow_suffix_link : Follows the suffix link of the source node according
	 * to Ukkonen's rules.
	 * 
	 * Input : The tree, and pos. pos is a combination of the source node and
	 * the position in its incoming edge where suffix ends. Output: The
	 * destination node that represents the longest suffix of node's path.
	 * Example: if node represents the path "abcde" then it returns the node
	 * that represents "bcde".
	 */

	void follow_suffix_link(SUFFIX_TREE tree, POS pos)
	{
		/*
		 * gama is the string between node and its father, in case node doesn't
		 * have a suffix link
		 */
		PATH gama = new PATH();
		/* dummy argument for trace_string function */
		MyInteger chars_found = new MyInteger(0);

		if (pos.node == tree.root) {
			return;
		}

		/*
		 * If node has no suffix link yet or in the middle of an edge - remember
		 * the edge between the node and its father (gama) and follow its
		 * father's suffix link (it must have one by Ukkonen's lemma). After
		 * following, trace down gama - it must exist in the tree (and thus can
		 * use the skip trick - see trace_string function description)
		 */
		if (pos.node.suffix_link == null || is_last_char_in_edge(tree, pos.node, pos.edge_pos.intValue) == 0) {
			/*
			 * If the node's father is the root, than no use following it's link
			 * (it is linked to itself). Tracing from the root (like in the
			 * naive algorithm) is required and is done by the calling function
			 * SEA uppon recieving a return value of tree.root from this
			 * function
			 */
			if (pos.node.father == tree.root) {
				pos.node = tree.root;
				return;
			}

			/* Store gama - the indices of node's incoming edge */
			gama.begin = pos.node.edge_label_start;
			gama.end = pos.node.edge_label_start + pos.edge_pos.intValue;
			/* Follow father's suffix link */
			pos.node = pos.node.father.suffix_link;
			/* Down-walk gama back to suffix_link's son */

			// clone the PATH
			PATH tmpGama = new PATH();
			tmpGama.begin = gama.begin;
			tmpGama.end = gama.end;
			pos.node = trace_string(tree, pos.node, tmpGama, (pos.edge_pos), chars_found, SKIP_TYPE.skip);
		}
		else {
			/* If a suffix link exists - just follow it */
			pos.node = pos.node.suffix_link;
			pos.edge_pos.intValue = get_node_label_length(tree, pos.node) - 1;
		}
	}

	/******************************************************************************/
	/*
	 * create_suffix_link : Creates a suffix link between node and the node
	 * 'link' which represents its largest suffix. The function could be avoided
	 * but is needed to monitor the creation of suffix links when debuging or
	 * changing the tree.
	 * 
	 * Input : The node to link from, the node to link to.
	 * 
	 * Output: None.
	 */

	void create_suffix_link(NODE node, NODE link)
	{
		node.suffix_link = link;
	}

	/******************************************************************************/
	/*
	 * SEA : Single-Extension-Algorithm (see Ukkonen's algorithm). Ensure that a
	 * certain extension is in the tree.
	 * 
	 * 1. Follows the current node's suffix link. 2. Check whether the rest of
	 * the extension is in the tree. 3. If it is - reports the calling function
	 * SPA of rule 3 (= current phase is done). 4. If it's not - inserts it by
	 * applying rule 2.
	 * 
	 * Input : The tree, pos - the node and position in its incoming edge where
	 * extension begins, str - the starting and ending indices of the extension,
	 * a flag indicating whether the last phase ended by rule 3 (last extension
	 * of the last phase already existed in the tree - and if so, the current
	 * phase starts at not following the suffix link of the first extension).
	 * 
	 * Output: The rule that was applied to that extension. Can be 3 (phase is
	 * done) or 2 (a new leaf was created).
	 */

	void SEA(SUFFIX_TREE tree, POS pos, PATH str, MyInteger rule_applied, char after_rule_3)
	{
		MyInteger chars_found = new MyInteger(0);
		int path_pos = str.begin;
		NODE tmp;

		// for DEBUG ST_PrintTree(tree);
		// for DEBUG
		// System.out.print("extension: "+str.begin+"  phase+1: "+str.end);
		// DEBUG
		// if(after_rule_3 == 0)
		// System.out.print("   followed from ("+pos.node.edge_label_start+","+get_node_label_end(tree,pos.node)+" | "+pos.edge_pos+") ");
		// else
		// System.out.print("   starting at ("+pos.node.edge_label_start+","+get_node_label_end(tree,pos.node)+" | "+pos.edge_pos+") ");

		counter++;

		/*
		 * Follow suffix link only if it's not the first extension after rule 3
		 * was applied
		 */
		if (after_rule_3 == 0)
			follow_suffix_link(tree, pos);

		// DEBUG
		// if(after_rule_3 == 0)
		// System.out.print("to ("+pos.node.edge_label_start+","+get_node_label_end(tree,pos.node)+" | "+pos.edge_pos+"). counter: "+counter+"\n");
		// else
		// System.out.print(". counter: "+counter+"\n");

		/*
		 * If node is root - trace whole string starting from the root, else -
		 * trace last character only
		 */
		if (pos.node == tree.root) {
			// CLONE STR
			PATH tmpStr = new PATH();
			tmpStr.begin = str.begin;
			tmpStr.end = str.end;

			pos.node = trace_string(tree, tree.root, tmpStr, (pos.edge_pos), chars_found, SKIP_TYPE.no_skip);
		}
		else {
			str.begin = str.end;
			chars_found.intValue = 0;

			/*
			 * Consider 2 cases: 1. last character matched is the last of its
			 * edge
			 */
			if (is_last_char_in_edge(tree, pos.node, pos.edge_pos.intValue) != 0) {
				/* Trace only last symbol of str, search in the NEXT edge (node) */
				tmp = find_son(tree, pos.node, tree.tree_string.charAt(str.end));
				if (tmp != null) {
					pos.node = tmp;
					pos.edge_pos.intValue = 0;
					chars_found.intValue = 1;
				}
			}
			/* 2. last character matched is NOT the last of its edge */
			else {
				/*
				 * Trace only last symbol of str, search in the CURRENT edge
				 * (node)
				 */
				if (tree.tree_string.charAt(pos.node.edge_label_start + pos.edge_pos.intValue + 1) == tree.tree_string.charAt(str.end)) {
					pos.edge_pos.intValue++;
					chars_found.intValue = 1;
				}
			}
		}

		/* If whole string was found - rule 3 applies */
		if (chars_found.intValue == str.end - str.begin + 1) {
			rule_applied.intValue = 3;
			/*
			 * If there is an internal node that has no suffix link yet (only
			 * one may exist) - create a suffix link from it to the father-node
			 * of the current position in the tree (pos)
			 */
			if (suffixless != null) {
				create_suffix_link(suffixless, pos.node.father);
				/* Marks that no internal node with no suffix link exists */
				suffixless = null;
			}

			// for DEBUG
			// System.out.print("rule 3 ("+str.begin+","+str.end+")\n");
			return;
		}

		/*
		 * If last char found is the last char of an edge - add a character at
		 * the next edge
		 */
		if (is_last_char_in_edge(tree, pos.node, pos.edge_pos.intValue) != 0 || pos.node == tree.root) {
			/* Decide whether to apply rule 2 (new_son) or rule 1 */
			if (pos.node.sons != null) {
				/*
				 * Apply extension rule 2 new son - a new leaf is created and
				 * returned by apply_extension_rule_2
				 */
				apply_extension_rule_2(pos.node, str.begin + chars_found.intValue, str.end, path_pos, 0, RULE_2_TYPE.new_son);
				rule_applied.intValue = 2;
				/*
				 * If there is an internal node that has no suffix link yet
				 * (only one may exist) - create a suffix link from it to the
				 * father-node of the current position in the tree (pos)
				 */
				if (suffixless != null) {
					create_suffix_link(suffixless, pos.node);
					/* Marks that no internal node with no suffix link exists */
					suffixless = null;
				}
			}
		}
		else {
			/*
			 * Apply extension rule 2 split - a new node is created and returned
			 * by apply_extension_rule_2
			 */
			tmp = apply_extension_rule_2(pos.node, str.begin + chars_found.intValue, str.end, path_pos, pos.edge_pos.intValue, RULE_2_TYPE.split);
			if (suffixless != null)
				create_suffix_link(suffixless, tmp);
			/* Link root's sons with a single character to the root */
			if (get_node_label_length(tree, tmp) == 1 && tmp.father == tree.root) {
				tmp.suffix_link = tree.root;
				/* Marks that no internal node with no suffix link exists */
				suffixless = null;
			}
			else
				/* Mark tmp as waiting for a link */
				suffixless = tmp;

			/* Prepare pos for the next extension */
			pos.node = tmp;
			rule_applied.intValue = 2;
		}
	}

	/******************************************************************************/
	/*
	 * SPA : Performs all insertion of a single phase by calling function SEA
	 * starting from the first extension that does not already exist in the tree
	 * and ending at the first extension that already exists in the tree.
	 * 
	 * Input :The tree, pos - the node and position in its incoming edge where
	 * extension begins, the phase number, the first extension number of that
	 * phase, a flag signaling whether the extension is the first of this phase,
	 * after the last phase ended with rule 3. If so - extension will be
	 * executed again in this phase, and thus its suffix link would not be
	 * followed.
	 * 
	 * Output:The extension number that was last executed on this phase. Next
	 * phase will start from it and not from 1
	 */

	/**
	 * 
	 * @param tree
	 *            The tree
	 * @param pos
	 *            Current node
	 * @param phase
	 *            Current phase number
	 * @param extension
	 *            The last extension performed in the previous phase
	 * @param repeated_extension
	 *            1 if the last rule applied is 3
	 */
	void SPA(SUFFIX_TREE tree, POS pos, int phase, MyInteger extension, MyChar repeated_extension)
	{
		/* No such rule (0). Used for entering the loop */
		MyInteger rule_applied = new MyInteger(0);
		PATH str = new PATH();

		/* Leafs Trick: Apply implicit extensions 1 through prev_phase */
		tree.e = phase + 1;

		/*
		 * Apply explicit extensions untill last extension of this phase is
		 * reached or extension rule 3 is applied once
		 */
		while (extension.intValue <= phase + 1) {
			str.begin = extension.intValue;
			str.end = phase + 1;

			/* Call Single-Extension-Algorithm */

			// clone of PATH
			PATH tmpStr = new PATH();
			tmpStr.begin = str.begin;
			tmpStr.end = str.end;

			SEA(tree, pos, tmpStr, rule_applied, repeated_extension.chValue);

			/* Check if rule 3 was applied for the current extension */
			if (rule_applied.intValue == 3) {
				/*
				 * Signaling that the next phase's first extension will not
				 * follow a suffix link because same extension is repeated
				 */
				repeated_extension.chValue = 1;
				break;
			}
			repeated_extension.chValue = 0;
			(extension.intValue)++;
		}
		return;
	}

	/******************************************************************************/
	/*
	 * ST_CreateTree : Allocates memory for the tree and starts Ukkonen's
	 * construction algorithm by calling SPA n times, where n is the length of
	 * the source string.
	 * 
	 * Input : The source string and its length. The string is a sequence of
	 * unsigned characters (maximum of 256 different symbols) and not
	 * null-terminated. The only symbol that must not appear in the string is $
	 * (the dollar sign). It is used as a unique symbol by the algorithm ans is
	 * appended automatically at the end of the string (by the program, not by
	 * the user!). The meaning of the $ sign is connected to the
	 * implicit/explicit suffix tree transformation, detailed in Ukkonen's
	 * algorithm.
	 * 
	 * Output: A pointer to the newly created tree. Keep this pointer in order
	 * to perform operations like search and delete on that tree. Obviously, no
	 * de-allocating of the tree space could be done if this pointer is lost, as
	 * the tree is allocated dynamically on the heap.
	 */

	SUFFIX_TREE ST_CreateTree(String str, int length)
	{
		SUFFIX_TREE tree;
		int phase;
		MyInteger extension = new MyInteger(0);
		MyChar repeated_extension = new MyChar(0);
		POS pos = new POS();

		if (str == null)
			return null;

		/* Allocating the tree */
		tree = new SUFFIX_TREE();

		// heap+=sizeof(SUFFIX_TREE);

		/* Calculating string length (with an ending $ sign) */
		tree.length = length + 1;
		ST_ERROR = length + 10;

		/* Allocating the only real string of the tree */
		tree.tree_string = "";

		// heap+=(tree.length+1)*sizeof(char);

		// memcpy(tree.tree_string+sizeof(char),str,length*sizeof(char));
		tree.tree_string = " " + str.substring(0, length) + "$";

		/* $ is considered a uniqe symbol */
		// tree.tree_string.charAt(tree.length) = '$';

		/* Allocating the tree root node */
		tree.root = create_node(null, 0, 0, 0);
		tree.root.suffix_link = null;

		/* Initializing algorithm parameters */
		extension.intValue = 2;
		phase = 2;

		/*
		 * Allocating first node, son of the root (phase 0), the longest path
		 * node
		 */
		tree.root.sons = create_node(tree.root, 1, tree.length, 1);
		suffixless = null;
		pos.node = tree.root;
		pos.edge_pos.intValue = 0;

		/* Ukkonen's algorithm begins here */
		for (; phase < tree.length; phase++) {
			/* Perform Single Phase Algorithm */
			SPA(tree, pos, phase, extension, repeated_extension);
		}
		return tree;
	}

	/******************************************************************************/
	/*
	 * ST_PrintNode : Prints a subtree under a node of a certain tree-depth.
	 * 
	 * Input : The tree, the node that is the root of the subtree, and the depth
	 * of that node. The depth is used for printing the branches that are coming
	 * from higher nodes and only then the node itself is printed. This gives
	 * the effect of a tree on screen. In each recoursive call, the depth is
	 * increased.
	 * 
	 * Output: A printout of the subtree to the screen.
	 */

	void ST_PrintNode(SUFFIX_TREE tree, NODE node1, int depth)
	{
		NODE node2 = node1.sons;
		int d = depth, start = node1.edge_label_start, end;
		end = get_node_label_end(tree, node1);

		if (depth > 0) {
			/* Print the branches coming from higher nodes */
			while (d > 1) {
				System.out.print("|");
				d--;
			}
			System.out.print("+");
			/* Print the node itself */
			while (start <= end) {
				System.out.print(tree.tree_string.charAt(start));
				start++;
			}
			// for DEBUG
			// System.out.print("  \t\t\t("+node1.edge_label_start+","+end+" | "+node1.path_position+")"
			// );
			System.out.print("\n");
		}
		/* Recoursive call for all node1's sons */
		while (node2 != null) {
			ST_PrintNode(tree, node2, depth + 1);
			node2 = node2.right_sibling;
		}
	}

	/******************************************************************************/
	/*
	 * ST_PrintFullNode : This function prints the full path of a node, starting
	 * from the root. It calls itself recoursively and than prints the last
	 * edge.
	 * 
	 * Input : the tree and the node its path is to be printed.
	 * 
	 * Output: Prints the path to the screen, no return value.
	 */

	void ST_PrintFullNode(SUFFIX_TREE tree, NODE node)
	{
		int start, end;
		if (node == null)
			return;
		/* Calculating the begining and ending of the last edge */
		start = node.edge_label_start;
		end = get_node_label_end(tree, node);

		/* Stoping condition - the root */
		if (node.father != tree.root)
			ST_PrintFullNode(tree, node.father);
		/* Print the last edge */
		while (start <= end) {
			System.out.print(tree.tree_string.charAt(start));
			start++;
		}
	}

	/******************************************************************************/
	/*
	 * ST_PrintTree : This function prints the tree. It simply starts the
	 * recoursive function ST_PrintNode with depth 0 (the root).
	 * 
	 * Input : The tree to be printed.
	 * 
	 * Output: A print out of the tree to the screen.
	 */

	void ST_PrintTree(SUFFIX_TREE tree)
	{
		System.out.print("\nroot\n");
		ST_PrintNode(tree, tree.root, 0);
	}

	/******************************************************************************/
	/*
	 * ST_FindSubstring : See suffix_tree.h for description.
	 */

	/**
	 * 
	 * @param tree
	 *            The suffix array
	 * @param W
	 *            The substring to find
	 * @param P
	 *            The length of W
	 * @return
	 */
	int ST_FindSubstring(SUFFIX_TREE tree, String W, int P)
	{
		/*
		 * Starts with the root's son that has the first character of W as its
		 * incoming edge first character
		 */
		NODE node = find_son(tree, tree.root, W.charAt(0));
		int k, j = 0, node_label_end;

		/*
		 * Scan nodes down from the root untill a leaf is reached or the
		 * substring is found
		 */
		while (node != null) {
			k = node.edge_label_start;
			node_label_end = get_node_label_end(tree, node);

			/*
			 * Scan a single edge - compare each character with the searched
			 * string
			 */
			while (j < P && k <= node_label_end && tree.tree_string.charAt(k) == W.charAt(j)) {
				j++;
				k++;

				/*
				 * #ifdef STATISTICS counter++; #endif
				 */
			}

			/* Checking which of the stopping conditions are true */
			if (j == P) {
				/*
				 * W was found - it is a substring. Return its path starting
				 * index
				 */
				return node.path_position;
			}
			else if (k > node_label_end)
				/* Current edge is found to match, continue to next edge */
				node = find_son(tree, node, W.charAt(j));
			else {
				/* One non-matching symbols is found - W is not a substring */
				return ST_ERROR;
			}
		}
		return ST_ERROR;
	}

	/******************************************************************************/
	/*
	 * ST_SelfTest : Self test of the tree - search for all substrings of the
	 * main string. See testing paragraph in the readme.txt file.
	 * 
	 * Input : The tree to test.
	 * 
	 * Output: 1 for success and 0 for failure. Prints a result message to the
	 * screen.
	 */

	int ST_SelfTest(SUFFIX_TREE tree)
	{
		int k, j, i;

		/*
		 * #ifdef STATISTICS int old_counter = counter; #endif
		 */

		/* Loop for all the prefixes of the tree source string */
		for (k = 1; k < tree.length; k++) {
			/* Loop for each suffix of each prefix */
			for (j = 1; j <= k; j++) {
				/*
				 * #ifdef STATISTICS counter = 0; #endif
				 */
				/* Search the current suffix in the tree */
				String str = tree.tree_string.substring(j);
				i = ST_FindSubstring(tree, str, k - j + 1);
				if (i == ST_ERROR) {
					System.out.print("\n\nTest Results: Fail in string (" + j + "," + k + ").\n\n");
					return 0;
				}
			}
		}
		/*
		 * #ifdef STATISTICS counter = old_counter; #endif
		 */
		/* If we are here no search has failed and the test passed successfuly */
		System.out.print("\n\nTest Results: Success.\n\n");
		return 1;
	}

	public static void main(String[] args)
	{
		/* Will hold the position of the substring if exists in the tree. */
		int position;

		SuffixTreeC app = new SuffixTreeC();
		/* Create the suffix tree */
		String str = "mississippi";

		// abba#xbac!bbxb*acyde@a00
		SUFFIX_TREE tree = app.ST_CreateTree(str, str.length());

		/* Print the suffix tree. */
		app.ST_PrintTree(tree);

		String toFind = "ssi";
		position = app.ST_FindSubstring(tree, toFind, toFind.length());
		System.out.println("the position of " + toFind + " is " + position);
	}
}

class MyChar
{
	char	chValue;

	public MyChar(int a)
	{
		chValue = (char) a;
	}

	public String toString()
	{
		return "" + chValue;
	}
}

class MyInteger
{
	int	intValue;

	public MyInteger(int a)
	{
		intValue = a;
	}

	public String toString()
	{
		return "" + intValue;
	}
}

/******************************************************************************/
/* DATA STRUCTURES */
/******************************************************************************/
/* This structure describes a node and its incoming edge */
class NODE
{
	/* A linked list of sons of that node */
	NODE	sons;
	/* A linked list of right siblings of that node */
	NODE	right_sibling;
	/* A linked list of left siblings of that node */
	NODE	left_sibling;
	/* A pointer to that node's father */
	NODE	father;
	/*
	 * A pointer to the node that represents the largest suffix of the current
	 * node
	 */
	NODE	suffix_link;
	/* Index of the start position of the node's path */
	int		path_position;
	/* Start index of the incoming edge */
	int		edge_label_start;
	/* End index of the incoming edge */
	int		edge_label_end;
};

/* This structure describes a suffix tree */
class SUFFIX_TREE
{
	/* The virtual end of all leaves */
	int		e;
	/*
	 * The one and only real source string of the tree. All edge-labels contain
	 * only indices to this string and do not contain the characters themselves
	 */
	String	tree_string;
	/* The length of the source string */
	int		length;
	/*
	 * The node that is the head of all others. It has no siblings nor a father
	 */
	NODE	root;
}

/* Used in function trace_string for skipping (Ukkonen's Skip Trick). */
enum SKIP_TYPE
{
	skip, no_skip
};

/*
 * Used in function apply_rule_2 - two types of rule 2 - see function for more
 * details.
 */
enum RULE_2_TYPE
{
	new_son, split
};

/* Signals whether last matching position is the last one of the current edge */
enum LAST_POS_TYPE
{
	last_char_in_edge, other_char
};

class PATH
{
	int	begin;
	int	end;
}

class POS
{
	NODE		node;
	MyInteger	edge_pos	= new MyInteger(0);
}