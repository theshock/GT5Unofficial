package idealindustrial.textures;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import idealindustrial.util.misc.II_StreamUtil;

import java.util.Arrays;

public class TextureUtil {
    public static final TextureConfiguration facing1Configuration = new MachineTextureConfig(true,"bottom", "top", "side", "out");
    public static final TextureConfiguration facing2Configuration = new MachineTextureConfig(true,"bottom", "top", "side", "out", "main");

    public static class MachineTextureConfig implements TextureConfiguration {
        public final String[] textureNames;

        public MachineTextureConfig(boolean hasActive, String... textureNames) {
            if (hasActive) {
                this.textureNames = II_StreamUtil.concatArrays(textureNames, Arrays.stream(textureNames).map(s -> s + "_active").toArray(String[]::new));
            }
            else {
                this.textureNames = textureNames;
            }
        }

        @Override
        public IIconContainer[] loadAll(String prefixPath) {
            IIconContainer[] loaded = new IIconContainer[textureNames.length];
            for (int i = 0; i < loaded.length; i++) {
                loaded[i] = TextureManager.INSTANCE.blockTexture(prefixPath + textureNames[i]);
            }
            return loaded;
        }
    }

    public static class BlockTextureConfig implements TextureConfiguration {
        int toLoad;

        public BlockTextureConfig(int toLoad) {
            this.toLoad = toLoad;
        }

        @Override
        public IIconContainer[] loadAll(String prefixPath) {
            IIconContainer[] loaded = new IIconContainer[toLoad];
            for (int i = 0; i < loaded.length; i++) {
                loaded[i] = TextureManager.INSTANCE.blockTexture(prefixPath + i);
            }
            return loaded;
        }
    }

    public static ITexture[] asGtRendered(IIconContainer... containers) {
        return asGtRendered(containers, new short[]{255, 255, 255, 0});
    }

    public static ITexture[] asGtRendered(IIconContainer[] containers, short[] rgba) {
        return Arrays.stream(containers).map(c -> new GT_RenderedTexture(c, rgba)).toArray(ITexture[]::new);
    }

    public static ITexture[] loadTextures(TextureConfiguration configuration, String path) {
        return asGtRendered(configuration.loadAll(path));
    }
}
