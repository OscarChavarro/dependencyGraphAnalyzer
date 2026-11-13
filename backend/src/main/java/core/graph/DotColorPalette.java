package core.graph;

/**
 * Colors used when exporting Graphviz .dot files.
 *
 * Always hexadecimal: Graphviz resolves color names with the X11 scheme when
 * rasterizing (PNG), but copies them verbatim into SVG output, where browsers
 * resolve them with the CSS scheme. Some names differ between both schemes
 * (e.g. "green" is #00FF00 in X11 but #008000 in CSS), so named colors make the
 * PNG and the SVG shown in the frontend look different. Values below keep the
 * original X11 appearance.
 */
public final class DotColorPalette {
    public static final String OUTLINE = "\"#000000\"";
    public static final String TOP_LEVEL_NODE_FILL = "\"#00FF00\"";
    public static final String SINK_NODE_FILL = "\"#ADD8E6\"";
    public static final String VARIANT_NODE_FILL = "\"#ADD8E6\"";
    public static final String INTERMEDIATE_NODE_FILL = "\"#FFFF00\"";
    public static final String BAD_NODE_FILL = "\"#FF0000\"";
    public static final String GROUP_CLUSTER_FILL = "\"#D3D3D3\"";
    public static final String BACKWARD_GROUP_EDGE = "\"#FF0000\"";

    private DotColorPalette() {
    }
}
