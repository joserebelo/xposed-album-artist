package me.joserebelo.musicalbumartist;

import de.robv.android.xposed.IXposedHookZygoteInit;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class XposedMod implements IXposedHookZygoteInit {
    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        try {
            Class<?> c = findClass("android.media.MediaScanner$MyMediaScannerClient", null);

            XposedBridge.hookAllMethods(c, "endFile", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String mAlbumArtist = (String) XposedHelpers.getObjectField(param.thisObject, "mAlbumArtist");

                    if (mAlbumArtist != null && mAlbumArtist.length() != 0)
                        XposedHelpers.setObjectField(param.thisObject, "mArtist", mAlbumArtist);
                }
            });
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }
}
