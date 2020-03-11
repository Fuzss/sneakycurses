var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');

function initializeCoreMod() {
    return {
        // make infinity compatible with mending
        'infinity_enchantment_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.enchantment.InfinityEnchantment'
            },
            'transformer': function(classNode) {
                patch([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patch: patchInfinityEnchantmentCanApplyTogether
                }], classNode, "InfinityEnchantment");
                return classNode;
            }
        },
        // make impaling incompatible with all damage enchantments
        'damage_enchantment_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.enchantment.DamageEnchantment'
            },
            'transformer': function(classNode) {
                patch([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patch: patchDamageEnchantmentCanApplyTogether
                }], classNode, "DamageEnchantment");
                return classNode;
            }
        },
        // properly allow infinity enchantment on crossbows
        'crossbow_item_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.item.CrossbowItem'
            },
            'transformer': function(classNode) {
                patch([{
                    obfName: "func_220021_a",
                    name: "hasAmmo",
                    desc: "(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)Z",
                    patch: patchCrossbowItemHasAmmo
                }, {
                    obfName: "func_220024_a",
                    name: "createArrow",
                    desc: "(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/AbstractArrowEntity;",
                    patch: patchCrossbowItemCreateArrow
                }], classNode, "CrossbowItem");
                return classNode;
            }
        },
        // enable piercing enchantment on bows
        'bow_item_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.item.BowItem'
            },
            'transformer': function(classNode) {
                patch([{
                    obfName: "func_77615_a",
                    name: "onPlayerStoppedUsing",
                    desc: "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
                    patch: patchBowItemOnPlayerStoppedUsing
                }], classNode, "BowItem");
                return classNode;
            }
        }
    };
}

function findMethod(methods, entry) {
    var length = methods.length;
    for (var i = 0; i < length; i++) {
        var method = methods[i];
        if ((method.name.equals(entry.obfName) || method.name.equals(entry.name)) && method.desc.equals(entry.desc)) {
            return method;
        }
    }
    return null;
}

function patch(entries, classNode, name) {
    log("Patching " + name + "...");
    for (var i = 0; i < entries.length; i++) {
        var entry = entries[i];
        var method = findMethod(classNode.methods, entry);
        if (method !== null) {
            var obfuscated = method.name.equals(entry.obfName);
            var flag = entry.patch(method, obfuscated);
        }
        if (flag) {
            log("Patching " + name + "#" + entry.name + " was successful");
        } else {
            log("Patching " + name + "#" + entry.name + " failed");
        }
    }
}

function patchBowItemOnPlayerStoppedUsing(method, obfuscated) {
    var addEntity = obfuscated ? "func_217376_c" : "addEntity";
    var foundNode = null;
    var instructions = method.instructions.toArray();
    var length = instructions.length;
    for (var i = 0; i < length; i++) {
        var node = instructions[i];
        if (node instanceof MethodInsnNode && node.getOpcode().equals(Opcodes.INVOKEVIRTUAL) && node.owner.equals("net/minecraft/world/World") && node.name.equals(addEntity) && node.desc.equals("(Lnet/minecraft/entity/Entity;)Z")) {
            var nextNode = node.getPrevious();
            if (nextNode instanceof VarInsnNode && nextNode.getOpcode().equals(Opcodes.ALOAD) && nextNode.var.equals(12)) {
                foundNode = node;
                break;
            }
        }
    }
    if (foundNode != null) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 12));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/fuzs/vanillaflavouredmagic/asm/Hooks", "applyBowEnchantments", "(Lnet/minecraft/entity/projectile/AbstractArrowEntity;Lnet/minecraft/item/ItemStack;)V", false));
        method.instructions.insertBefore(getNthNode(node, -2), insnList);
        return true;
    }
}

function patchCrossbowItemCreateArrow(method, obfuscated) {
    var foundNode = null;
    var instructions = method.instructions.toArray();
    var length = instructions.length;
    for (var i = 0; i < length; i++) {
        var node = instructions[i];
        if (node instanceof VarInsnNode && node.getOpcode().equals(Opcodes.ALOAD) && node.var.equals(5)) {
            var nextNode = node.getNext();
            if (nextNode instanceof InsnNode && nextNode.getOpcode().equals(Opcodes.ARETURN)) {
                foundNode = node;
                break;
            }
        }
    }
    if (foundNode != null) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 5));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/fuzs/vanillaflavouredmagic/asm/Hooks", "applyCrossbowEnchantments", "(Lnet/minecraft/entity/projectile/AbstractArrowEntity;Lnet/minecraft/item/ItemStack;)V", false));
        method.instructions.insertBefore(foundNode, insnList);
        return true;
    }
}

function patchCrossbowItemHasAmmo(method, obfuscated) {
    var foundNode = null;
    var instructions = method.instructions.toArray();
    var length = instructions.length;
    for (var i = 0; i < length; i++) {
        var node = instructions[i];
        if (node instanceof VarInsnNode && node.getOpcode().equals(Opcodes.ISTORE) && node.var.equals(4)) {
            var nextNode = getNthNode(node, -9);
            if (nextNode instanceof FieldInsnNode && nextNode.getOpcode().equals(Opcodes.GETFIELD) && nextNode.owner.equals("net/minecraft/entity/player/PlayerAbilities") && nextNode.name.equals("isCreativeMode") && nextNode.desc.equals("Z")) {
                foundNode = node;
                break;
            }
        }
    }
    if (foundNode != null) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/fuzs/vanillaflavouredmagic/asm/Hooks", "hasInfinity", "(ZLnet/minecraft/item/ItemStack;)Z", false));
        method.instructions.insertBefore(foundNode, insnList);
        return true;
    }
}

function patchDamageEnchantmentCanApplyTogether(method, obfuscated) {
    var foundNode = null;
    var instructions = method.instructions.toArray();
    var length = instructions.length;
    for (var i = 0; i < length; i++) {
        var node = instructions[i];
        if (node instanceof TypeInsnNode && node.getOpcode().equals(Opcodes.INSTANCEOF) && node.desc.equals("net/minecraft/enchantment/DamageEnchantment")) {
            var nextNode = node.getNext();
            if (nextNode instanceof JumpInsnNode) {
                foundNode = node;
                break;
            }
        }
    }
    if (foundNode != null) {
        var insnList = new InsnList();
        insnList.add(new JumpInsnNode(Opcodes.IFNE, getNthNode(foundNode, 1).label));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/enchantment/ImpalingEnchantment"));
        method.instructions.insert(foundNode, insnList);
        return true;
    }
}

function patchInfinityEnchantmentCanApplyTogether(method, obfuscated) {
    var canApplyTogether = obfuscated ? "func_77326_a" : "canApplyTogether";
    var instructions = method.instructions.toArray();
    if (instructions.length > 0) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/enchantment/Enchantment", canApplyTogether, "(Lnet/minecraft/enchantment/Enchantment;)Z", false));
        insnList.add(new InsnNode(Opcodes.IRETURN));
        method.instructions.insertBefore(instructions[0], insnList);
        return true;
    }
}

function getNthNode(node, n) {
    for (var i = 0; i < Math.abs(n); i++) {
        if (n < 0) {
            node = node.getPrevious();
        } else {
            node = node.getNext();
        }
    }
    return node;
}

function log(s) {
    print("[Sneaky Magic Transformer]: " + s);
}