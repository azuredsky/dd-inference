package logic.add_gen;

import graph.Graph;

import java.util.*;

// Note that although this node contains a local ID,
// it is just a referent to the actual node.  Many
// AADDRNodes can have the same local id but different
// offset and multiplier values - the AADDRNode is
// simply a wrapper.
public class AADDRNode {
    
    // This is assigned by an outer struct and gives
    // access to formula assoc with global-ID, is
    // analogy of prop var ID for ADDs.
    public int _nRefID;
    public Gen _aOffset, _aMult;
    
    public AADDRNode(int lid, Gen offset, Gen mult) {
	_nRefID    = lid;
	_aOffset   = offset;
	_aMult     = mult;
    }

    public void set(int lid, Gen offset, Gen mult) {
	_nRefID    = lid;
	_aOffset   = offset;
	_aMult     = mult;
    }
    
    public String toString(Object context, int depth) {
		StringBuffer sb = new StringBuffer();
		sb.append("[ <" + _aOffset + ","
				+ _aMult + "> ");

		if (context instanceof AADD) {
			sb.append(((AADD) context).getNode(_nRefID).toString(context,
					depth + 1));
		} else {
			return "[ ERROR: " + context.getClass() + " ] ";
		}
		sb.append("]");

		return sb.toString();
	}
    
    public void toGraph(Object context, Graph g) {
		g.addNodeLabel("ROOT", "ROOT");
		if (DD.USE_COLOR) {
			if (DD.USE_FESTIVE)
				g.addNodeColor("ROOT", "yellow");
			else
				g.addNodeColor("ROOT", "plum");
		}
		g.addNodeShape("ROOT", "diamond");
		g.addNodeStyle("ROOT", "filled");
		g.addUniLink("ROOT", "#" + _nRefID, "black", "dashed", 
				"<" + _aOffset.toString() + " + " + _aMult.toString() + " * >");

		if (context instanceof AADD) {
			((AADD) context).getNode(_nRefID).toGraph(context, g);
		} else {
			System.out.println("[ ERROR GENERATING GRAPH: " + context.getClass() + " ] ");
		}
	}
 
    public int hashCode() {
	// Perfect hash up to 2^15 = 32768 nodes				   
	long d1 = _aOffset.toRawLongBits();
	int  i1 = (int)(d1 + (d1 >> 32));
	long d2 = _aMult.toRawLongBits();
	int  i2 = (int)(d2 + (d2 >> 32));
	i2 = (i2 << 16) + (i2 >> 16);

	return _nRefID - i1 + i2;
    }
    
    public boolean equals(Object o) {
	AADDRNode s = (AADDRNode)o;
	return ((_nRefID == s._nRefID) && 
		(_aOffset  == s._aOffset) &&
		(_aMult    == s._aMult));
    }
}

