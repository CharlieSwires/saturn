import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/** Composes sprites and crisp HiDPI text before a single screen transfer. */
final class FrameRenderer {
    private BufferedImage frame;

    void draw(Graphics2D target, BufferedImage game, Consumer<Graphics2D> drawText) {
        AffineTransform transform = target.getTransform();
        double scaleX = Math.max(1.0, Math.hypot(transform.getScaleX(), transform.getShearY()));
        double scaleY = Math.max(1.0, Math.hypot(transform.getScaleY(), transform.getShearX()));
        int pixelWidth = (int) Math.ceil(game.getWidth() * scaleX);
        int pixelHeight = (int) Math.ceil(game.getHeight() * scaleY);
        if (frame == null || frame.getWidth() != pixelWidth || frame.getHeight() != pixelHeight)
            frame = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D composed = frame.createGraphics();
        try {
            composed.scale((double) pixelWidth / game.getWidth(),
                    (double) pixelHeight / game.getHeight());
            composed.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            composed.drawImage(game, 0, 0, null);
            drawText.accept(composed);
        } finally {
            composed.dispose();
        }
        target.drawImage(frame, 0, 0, game.getWidth(), game.getHeight(), null);
    }
}
