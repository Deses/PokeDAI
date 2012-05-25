package componentes;

import entidades.Bloque;
import entidades.Puerta;
import it.marteEngine.entity.Entity;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class Nivel {

    private final TiledMap tiledMap;
    private final List<Float> animationXPositions = new ArrayList<Float>();
    private final List<Float> animationYPositions = new ArrayList<Float>();
    private final List<Integer> animationLayer = new ArrayList<Integer>();
    private final List<Animation> animations = new ArrayList<Animation>();

    // Constructores //
    public Nivel(final TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    // Utilidades //
    /**
     * Método que recorre el TiledMap entero y añade los elementos deseados a un
     * ArrayList.
     *
     * @return ArrayList - Elementos del mapa que quiero representar.
     */
    public List<Entity> getEntities() {
        List<Entity> result = new ArrayList<Entity>();
        for (int x = 0; x < tiledMap.getWidth(); x++) {
            for (int y = 0; y < tiledMap.getHeight(); y++) {
                for (int l = 0; l < tiledMap.getLayerCount(); l++) {
                    int tileID = tiledMap.getTileId(x, y, 0);
                    if (tileID == 1) {
                        result.add(new Bloque(x, y, Bloque.PARED, true));
                    }
                    if (tileID == 2) {
                        result.add(new Puerta(x, y, Bloque.PUERTA, true));
                    }
                    if (tiledMap.getLayerProperty(l, "underPlayer", "-1").equals("1") && tiledMap.getTileImage(x, y, l) != null) {
                        result.add(new Bloque(x, y, Bloque.CAPA1, false, tiledMap.getTileImage(x, y, l)));
                    }
                    if (tiledMap.getLayerProperty(l, "underPlayer", "-1").equals("0") && tiledMap.getTileImage(x, y, l) != null) {
                        result.add(new Bloque(x, y, Bloque.CAPA2, false, tiledMap.getTileImage(x, y, l)));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Método que busca las animaciones en el TiledMap.
     *
     * @return ArrayList - Animaciones.
     */
    public List<Animation> findAnimations() {
        for (int x = 0; x < tiledMap.getWidth(); x++) {
            for (int y = 0; y < tiledMap.getHeight(); y++) {
                for (int i = 0; i < tiledMap.getLayerCount(); i++) {
                    int tileID = tiledMap.getTileId(x, y, i);
                    int numFrames = Integer.valueOf(tiledMap.getTileProperty(tileID, "numFrames", "-1"));
                    if (tiledMap.getTileProperty(tileID, "animate", "-1").equals("1")) {
                        animationXPositions.add(x * (float) tiledMap.getTileWidth());
                        animationYPositions.add(y * (float) tiledMap.getTileHeight());
                        animationLayer.add(i);
                        Image[] frames = new Image[numFrames];
                        tiledMap.getTileImage(x, y, numFrames);
                        int sheetX = Integer.valueOf(tiledMap.getTileProperty(tileID, "sheetX", "-1"));
                        int sheetY = Integer.valueOf(tiledMap.getTileProperty(tileID, "sheetY", "-1"));
                        for (int k = 0; k < numFrames; k++) {
                            frames[k] = tiledMap.getTileSet(0).tiles.getSubImage(sheetX + Integer.valueOf(tiledMap.getTileProperty(tileID, "frame" + String.valueOf(k + 1), "-1")), sheetY);
                            if (tiledMap.getTileProperty(tileID, "transparent", "-1").equals("1")) {
                                frames[k].setAlpha(0.6f);
                            }
                        }
                        animations.add(new Animation(frames, Integer.valueOf(tiledMap.getTileProperty(tileID, "duration", "-1"))));
                    }
                }
            }
        }
        return animations;
    }

    // Getters & Setters //
    public List<Integer> getAnimationLayer() {
        return animationLayer;
    }

    public List<Float> getAnimationXPositions() {
        return animationXPositions;
    }

    public List<Float> getAnimationYPositions() {
        return animationYPositions;
    }

    public List<Animation> getAnimations() {
        return animations;
    }
}
