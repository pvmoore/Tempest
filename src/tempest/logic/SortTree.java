package tempest.logic;

import tempest.data._ROW;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;

import java.sql.SQLException;
import java.util.ArrayList;

public class SortTree {
    private ValueExpression[] expr;
    private boolean[] directions;
    private int size;
    private Node root;

    class Node {
        _ROW row;
        Node left;
        Node right;

        public Node(_ROW r) { row = r; }
    }

    //////////////////////////////////////////////////////////////////////////////
    public SortTree(ValueExpression[] expr, boolean[] directions) {
        this.expr = expr;
        this.directions = directions;
    }

    //////////////////////////////////////////////////////////////////////////////
    public void add(_ROW row) throws SQLException {
        if(root == null) {
            root = new Node(row);
            //System.out.println("root="+root.row);
            return;
        }
        //
        TypedObject[] results = new TypedObject[expr.length];
        //System.out.println("expr.length="+expr.length);
        for(int i = 0; i < results.length; i++) {
            results[i] = expr[i].evaluate(row);
            //System.out.println("row="+row);
            //System.out.println("expr["+i+"]="+expr[i]);
            //System.out.println("results["+i+"]="+results[i]);
        }
        addLeaf(root, row, results);
    }

    public TypedObject get(int index) {
        return null;
    }

    public ArrayList getRowsInOrder() {
        ArrayList al = new ArrayList();
        if(root == null) return al;
        getRowsInOrder(al, root);
        return al;
    }

    public int size() { return size; }

    public void clear() {
        root = null;
        size = 0;
        expr = null;
        directions = null;
    }

    //////////////////////////////////////////////////////////////////////////////
    private void getRowsInOrder(ArrayList al, Node parent) {
        if(parent.left != null) {
            getRowsInOrder(al, parent.left);
        }
        al.add(parent.row);
        if(parent.right != null) {
            getRowsInOrder(al, parent.right);
        }
    }

    private void addLeaf(Node node, _ROW row, TypedObject[] results) throws SQLException {
        int i = 0;
        TypedObject result = null;
        while(i < expr.length) {
            result = expr[i].evaluate(node.row);
            if(result == null) {
                if(results[i] != null) {
                    break;
                }
            } else {
                if(!result.equals(results[i])) break;
            }
            i++;
        }
        //
        if(i == expr.length) {
            // exactly equal so go left
            //System.out.println("= going left");
            if(node.left != null) {
                addLeaf(node.left, row, results);
                return;
            }
            node.left = new Node(row);
            return;
        }
        //
        if(directions[i]) {
            // lower=left, higher=right
            if(results[i] == null || ((Comparable<TypedObject>)results[i]).compareTo(result) < 0) {
                //System.out.println("left");
                if(node.left != null) {
                    addLeaf(node.left, row, results);
                } else {
                    node.left = new Node(row);
                }
            } else {
                //System.out.println("right");
                if(node.right != null) {
                    addLeaf(node.right, row, results);
                } else {
                    node.right = new Node(row);
                }
            }
        } else {
            // lower=right, higher=left
            if(results[i] == null || ((Comparable<TypedObject>)results[i]).compareTo(result) < 0) {
                if(node.right != null) {
                    addLeaf(node.right, row, results);
                    return;
                }
                node.right = new Node(row);
            } else {
                if(node.left != null) {
                    addLeaf(node.left, row, results);
                    return;
                }
                node.left = new Node(row);
            }
        }
    }
}