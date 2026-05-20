package co.cubestudio;

import java.util.ArrayList;

public class SoftwarePackageNode
{
    private String name;
    private ArrayList<SoftwarePackageNode> children;

    private boolean isBad;
    private boolean mark;
    private boolean group;
    private boolean sink; // Node has no children
    private boolean source; // Node has no parents
    private boolean variant;
    private boolean boldName; // Visual mark to highlight name

    /// Pointer to group label node, null when this node is not on any
    /// group
    private SoftwarePackageNode parentGroupNode;

    /// Used only on group label nodes
    private ArrayList<SoftwarePackageNode> externalGroupsDependencies;

    public SoftwarePackageNode(String name)
    {
        this.name = new String(name);
        isBad = false;
        children = new ArrayList<SoftwarePackageNode>();
        mark = false;
        group = false;
        sink = false;
        source = false;
        parentGroupNode = null;
	variant = false;
	boldName = false;
        externalGroupsDependencies = new ArrayList<SoftwarePackageNode>();
    }

    public boolean
    getVariant() {
	return variant;
    }

    public void
    setVariant(boolean x) {
	variant = x;
    }

    public void addExternalGroupDependency(SoftwarePackageNode n)
    {
        int i;

        for ( i = 0; i < externalGroupsDependencies.size(); i++ ) {
            if ( n == externalGroupsDependencies.get(i) ) {
                return;
	    }
	}
        externalGroupsDependencies.add(n);
    }

    public void setParentGroupNode(SoftwarePackageNode p)
    {
        parentGroupNode = p;
    }

    public SoftwarePackageNode getParentGroupNode()
    {
        return parentGroupNode;
    }

    public void setBoldName(boolean bn) {
	this.boldName = bn;
    }

    public boolean getBoldName() {
	return boldName;
    }

    public void setMark(boolean mark)
    {
        this.mark = mark;
    }

    public boolean getMark()
    {
        return mark;
    }

    public void setGroup(boolean group)
    {
        this.group = group;
    }

    public boolean getGroup()
    {
        return group;
    }

    public void setSink(boolean sink)
    {
        this.sink = sink;
    }

    public boolean getSink()
    {
        return sink;
    }

    public void setSource(boolean source)
    {
        this.source = source;
    }

    public boolean getSource()
    {
        return source;
    }

    public void markAsBad()
    {
        isBad = true;
    }

    public boolean getIsBad()
    {
        return isBad;
    }

    public String getName()
    {
        return name;
    }

    public void addChild(SoftwarePackageNode c)
    {
        if (!children.contains(c)) {
            children.add(c);
        }
    }

    public ArrayList<SoftwarePackageNode> getChildren()
    {
        return children;
    }

    public ArrayList<SoftwarePackageNode> getExternalGroupDependencies()
    {
        return externalGroupsDependencies;
    }
}
