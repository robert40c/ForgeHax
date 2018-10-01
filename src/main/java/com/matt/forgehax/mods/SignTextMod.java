package com.matt.forgehax.mods;

import com.google.common.eventbus.Subscribe;
import com.matt.forgehax.Helper;
import com.matt.forgehax.asm.events.replacementhooks.InputEvent;
import com.matt.forgehax.util.mod.Category;
import com.matt.forgehax.util.mod.ToggleMod;
import com.matt.forgehax.util.mod.loader.RegisterMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Created by Babbaj on 9/16/2017.
 */
@RegisterMod
public class SignTextMod extends ToggleMod {
    public SignTextMod() { super(Category.MISC, "SignText", false, "get sign text"); }


    @Subscribe
    public void onInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) { // on middle click
            RayTraceResult result = MC.player.rayTrace(999, 0);
            if (result == null) return;
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                TileEntity tileEntity = MC.world.getTileEntity(result.getBlockPos());

                if (tileEntity instanceof TileEntitySign) {
                    TileEntitySign sign = (TileEntitySign) tileEntity;

                    int signTextLength = 0;
                    // find the first line from the bottom that isn't empty
                    for (int i = 3; i >= 0; i--) {
                        if (!sign.signText[i].getUnformattedText().isEmpty()) {
                            signTextLength = i + 1;
                            break;
                        }
                    }
                    if (signTextLength == 0) return; // if the sign is empty don't do anything

                    String[] lines = new String[signTextLength];

                    for (int i = 0; i < signTextLength; i++) {
                        lines[i] = sign.signText[i].getFormattedText().replace(TextFormatting.RESET.toString(), "");
                    }

                    String fullText = String.join("\n", lines);

                    Helper.printMessage("Copied sign");
                    setClipboardString(fullText);

                }
            }
        }

    }

    private static void setClipboardString(String stringIn) {
        StringSelection selection = new StringSelection(stringIn);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }


}
