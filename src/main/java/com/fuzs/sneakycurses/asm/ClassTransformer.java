package com.fuzs.sneakycurses.asm;

import com.fuzs.sneakycurses.SneakyCurses;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class ClassTransformer implements IClassTransformer {

    private static final String[] classesBeingTransformed =
            {
                    "net.minecraft.client.renderer.RenderItem",
                    "net.minecraft.client.renderer.entity.layers.LayerArmorBase"
            };

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        boolean isObfuscated = !name.equals(transformedName);
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, basicClass, isObfuscated) : basicClass;
    }

    private static byte[] transform(int index, byte[] basicClass, boolean isObfuscated) {

        SneakyCurses.LOGGER.info("Attempting to transform " + classesBeingTransformed[index]);

        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            switch (index) {
                case 0:
                    transformRenderItem(classNode, isObfuscated);
                    break;
                case 1:
                    transformLayerArmorBase(classNode, isObfuscated);
                    break;
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return basicClass;

    }

    private static void transformRenderItem(ClassNode playerClass, boolean isObfuscated) {

        final String RENDERITEM_RENDERITEM = isObfuscated ? "a" : "renderItem";
        final String RENDERITEM_RENDERITEM_DESCRIPTOR = isObfuscated ? "(Laip;Lcfy;)V" : "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V";
        final String RENDERITEM_RENDEREFFECT = isObfuscated ? "a" : "renderEffect";
        final String RENDERITEM_RENDEREFFECT_DESCRIPTOR = isObfuscated ? "(Lcfy;)V" : "(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V";
        boolean flag = false;

        for (MethodNode method : playerClass.methods) {

            if (method.name.equals(RENDERITEM_RENDERITEM) && method.desc.equals(RENDERITEM_RENDERITEM_DESCRIPTOR)) {

                InsnList newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(ALOAD, 1));
                newInstructions.add(new MethodInsnNode(INVOKESTATIC, "com/fuzs/sneakycurses/asm/Hooks", "setTargetStack", isObfuscated ? "(Laip;)V" : "(Lnet/minecraft/item/ItemStack;)V", false));

                method.instructions.insertBefore(method.instructions.getFirst(), newInstructions);

                flag = true;

            }


            if (method.name.equals(RENDERITEM_RENDEREFFECT) && method.desc.equals(RENDERITEM_RENDEREFFECT_DESCRIPTOR)) {

                for (AbstractInsnNode instruction : method.instructions.toArray()) {

                    if (instruction.getOpcode() == LDC && ((LdcInsnNode) instruction).cst.equals(-8372020)) {

                        InsnList newInstructions = new InsnList();

                        newInstructions.add(new MethodInsnNode(INVOKESTATIC, "com/fuzs/sneakycurses/asm/Hooks", "applyColor", "(I)I", false));

                        method.instructions.insert(instruction, newInstructions);

                        flag = true;

                    }

                }

            }
        }
        SneakyCurses.LOGGER.info(flag ? "Transformation successful" : "Transformation failed");
    }

    private static void transformLayerArmorBase(ClassNode playerClass, boolean isObfuscated) {

        final String LAYERARMORBASE_RENDERARMORLAYER = isObfuscated ? "a" : "renderArmorLayer";
        final String LAYERARMORBASE_RENDERARMORLAYER_DESCRIPTOR = isObfuscated ? "(Lvp;FFFFFFFLvl;)V" : "(Lnet/minecraft/entity/EntityLivingBase;FFFFFFFLnet/minecraft/inventory/EntityEquipmentSlot;)V";
        final String LAYERARMORBASE_RENDERENCHANTEDGLINT = isObfuscated ? "a" : "renderEnchantedGlint";
        final String LAYERARMORBASE_RENDERENCHANTEDGLINT_DESCRIPTOR = isObfuscated ? "(Lcaa;Lvp;Lbqf;FFFFFFF)V" : "(Lnet/minecraft/client/renderer/entity/RenderLivingBase;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/model/ModelBase;FFFFFFF)V";
        boolean flag = false;

        for (MethodNode method : playerClass.methods) {

            if (method.name.equals(LAYERARMORBASE_RENDERARMORLAYER) && method.desc.equals(LAYERARMORBASE_RENDERARMORLAYER_DESCRIPTOR)) {

                InsnList newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(ALOAD, 1));
                newInstructions.add(new VarInsnNode(ALOAD, 9));
                newInstructions.add(new MethodInsnNode(INVOKESTATIC, "com/fuzs/sneakycurses/asm/Hooks", "setTargetStack", isObfuscated ? "(Lvp;Lvl;)V" : "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/inventory/EntityEquipmentSlot;)V", false));

                method.instructions.insertBefore(method.instructions.getFirst(), newInstructions);

                flag = true;

            }


            if (method.name.equals(LAYERARMORBASE_RENDERENCHANTEDGLINT) && method.desc.equals(LAYERARMORBASE_RENDERENCHANTEDGLINT_DESCRIPTOR)) {

                for (AbstractInsnNode instruction : method.instructions.toArray()) {

                    if (instruction.getOpcode() == INVOKESTATIC && ((MethodInsnNode) instruction).owner.equals(isObfuscated ? "bus" : "net/minecraft/client/renderer/GlStateManager")
                            && ((MethodInsnNode) instruction).name.equals(isObfuscated ? "c" : "color") && ((MethodInsnNode) instruction).desc.equals("(FFFF)V") && ((LdcInsnNode) instruction.getPrevious().getPrevious()).cst.equals(0.608F)) {

                        InsnList newInstructions = new InsnList();

                        newInstructions.add(new MethodInsnNode(INVOKESTATIC, "com/fuzs/sneakycurses/asm/Hooks", "applyColor", "()V", false));

                        method.instructions.insert(instruction, newInstructions);

                        flag = true;

                    }

                }

            }
        }
        SneakyCurses.LOGGER.info(flag ? "Transformation successful" : "Transformation failed");
    }

}
