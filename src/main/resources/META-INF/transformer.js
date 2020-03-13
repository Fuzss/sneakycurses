var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var InvokeDynamicInsnNode = Java.type('org.objectweb.asm.tree.InvokeDynamicInsnNode');
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
                patchMethod([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patches: [patchInfinityEnchantmentCanApplyTogether]
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
                patchMethod([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patches: [patchDamageEnchantmentCanApplyTogether]
                }], classNode, "DamageEnchantment");
                return classNode;
            }
        },

        // make piercing compatible with multishot
        'piercing_enchantment_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.enchantment.PiercingEnchantment'
            },
            'transformer': function(classNode) {
                patchMethod([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patches: [patchPiercingEnchantmentCanApplyTogether]
                }], classNode, "PiercingEnchantment");
                return classNode;
            }
        },

        // make piercing compatible with multishot
        'multishot_enchantment_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.enchantment.MultishotEnchantment'
            },
            'transformer': function(classNode) {
                patchMethod([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patches: [patchMultishotEnchantmentCanApplyTogether]
                }], classNode, "MultishotEnchantment");
                return classNode;
            }
        },

        // make different types of protection compatible with each other
        'protection_enchantment_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.enchantment.ProtectionEnchantment'
            },
            'transformer': function(classNode) {
                patchMethod([{
                    obfName: "func_77326_a",
                    name: "canApplyTogether",
                    desc: "(Lnet/minecraft/enchantment/Enchantment;)Z",
                    patches: [patchProtectionEnchantmentCanApplyTogether]
                }], classNode, "ProtectionEnchantment");
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
                patchMethod([{
                    obfName: "func_220021_a",
                    name: "hasAmmo",
                    desc: "(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)Z",
                    patches: [patchCrossbowItemHasAmmo]
                }, {
                    obfName: "func_220024_a",
                    name: "createArrow",
                    desc: "(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/AbstractArrowEntity;",
                    patches: [patchCrossbowItemCreateArrow]
                }], classNode, "CrossbowItem");
                return classNode;
            }
        },

        // hide glint from items solely enchanted with curses
        'item_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.item.Item'
            },
            'transformer': function(classNode) {
                patchMethod([{
                    obfName: "func_77636_d",
                    name: "hasEffect",
                    desc: "(Lnet/minecraft/item/ItemStack;)Z",
                    patches: [patchItemHasEffect]
                }], classNode, "Item");
                return classNode;
            }
        }
    };
}

function patchMethod(entries, classNode, name) {

    log("Patching " + name + "...");
    for (var i = 0; i < entries.length; i++) {

        var entry = entries[i];
        var method = findMethod(classNode.methods, entry);
        var flag = !!method;
        if (flag) {

            var obfuscated = !method.name.equals(entry.name);
            for (var j = 0; j < entry.patches.length; j++) {

                var patch = entry.patches[j];
                if (!patchInstructions(method, patch.filter, patch.action, obfuscated)) {

                    flag = false;
                }
            }
        }

        log("Patching " + name + "#" + entry.name + (flag ? " was successful" : " failed"));
    }
}

function findMethod(methods, entry) {

    for (var i = 0; i < methods.length; i++) {

        var method = methods[i];
        if ((method.name.equals(entry.obfName) || method.name.equals(entry.name)) && method.desc.equals(entry.desc)) {

            return method;
        }
    }
}

function patchInstructions(method, filter, action, obfuscated) {

    var instructions = method.instructions.toArray();
    for (var i = 0; i < instructions.length; i++) {

        var node = filter(instructions[i], obfuscated);
        if (!!node) {

            break;
        }
    }

    if (!!node) {

        action(node, method.instructions, obfuscated);
        return true;
    }
}

var patchItemHasEffect = {
    filter: function(node, obfuscated) {
        if (matchesNode(node, "net/minecraft/item/ItemStack", obfuscated ? "func_77948_v" : "isEnchanted", "()Z")) {
            var nextNode = node.getNext();
            if (nextNode instanceof InsnNode && nextNode.getOpcode().equals(Opcodes.IRETURN)) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(generateHook("hasEffect", "(ZLnet/minecraft/item/ItemStack;)Z"));
        instructions.insert(node, insnList);
    }
};

var patchCrossbowItemCreateArrow = {
    filter: function(node, obfuscated) {
        if (node instanceof VarInsnNode && node.getOpcode().equals(Opcodes.ALOAD) && node.var.equals(5)) {
            var nextNode = node.getNext();
            if (nextNode instanceof InsnNode && nextNode.getOpcode().equals(Opcodes.ARETURN)) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 5));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
        insnList.add(generateHook("applyCrossbowEnchantments", "(Lnet/minecraft/entity/projectile/AbstractArrowEntity;Lnet/minecraft/item/ItemStack;)V"));
        instructions.insertBefore(node, insnList);
    }
};

var patchCrossbowItemHasAmmo = {
    filter: function(node, obfuscated) {
        if (node instanceof VarInsnNode && node.getOpcode().equals(Opcodes.ISTORE) && node.var.equals(4)) {
            var nextNode = getNthNode(node, -9);
            if (nextNode instanceof FieldInsnNode && nextNode.getOpcode().equals(Opcodes.GETFIELD) && nextNode.owner.equals("net/minecraft/entity/player/PlayerAbilities") && nextNode.name.equals(obfuscated ? "field_75098_d" : "isCreativeMode") && nextNode.desc.equals("Z")) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(generateHook("hasInfinity", "(ZLnet/minecraft/item/ItemStack;)Z"));
        instructions.insertBefore(node, insnList);
    }
};

var patchProtectionEnchantmentCanApplyTogether = {
    filter: function(node, obfuscated) {
        if (node instanceof TypeInsnNode && node.getOpcode().equals(Opcodes.INSTANCEOF) && node.desc.equals("net/minecraft/enchantment/ProtectionEnchantment")) {
            var nextNode = node.getNext();
            if (nextNode instanceof JumpInsnNode && nextNode.getOpcode().equals(Opcodes.IFEQ)) {
                return nextNode;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(generateHook("canApplyProtection", "()Z"));
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, node.label));
        instructions.insert(node, insnList);
    }
};

var patchMultishotEnchantmentCanApplyTogether = {
    filter: function(node, obfuscated) {
        if (matchesNode(node, "net/minecraft/enchantment/Enchantment", obfuscated ? "func_77326_a" : "canApplyTogether", "(Lnet/minecraft/enchantment/Enchantment;)Z")) {
            var nextNode = node.getPrevious();
            if (nextNode instanceof VarInsnNode && nextNode.getOpcode().equals(Opcodes.ALOAD) && nextNode.var.equals(1)) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(generateHook("canApplyMultishot", "(ZLnet/minecraft/enchantment/Enchantment;)Z"));
        insnList.add(new InsnNode(Opcodes.IRETURN));
        instructions.insert(node, insnList);
    }
};

var patchPiercingEnchantmentCanApplyTogether = {
    filter: function(node, obfuscated) {
        if (matchesNode(node, "net/minecraft/enchantment/Enchantment", obfuscated ? "func_77326_a" : "canApplyTogether", "(Lnet/minecraft/enchantment/Enchantment;)Z")) {
            var nextNode = node.getPrevious();
            if (nextNode instanceof VarInsnNode && nextNode.getOpcode().equals(Opcodes.ALOAD) && nextNode.var.equals(1)) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(generateHook("canApplyPiercing", "(ZLnet/minecraft/enchantment/Enchantment;)Z"));
        insnList.add(new InsnNode(Opcodes.IRETURN));
        instructions.insert(node, insnList);
    }
};

var patchDamageEnchantmentCanApplyTogether = {
    filter: function(node, obfuscated) {
        if (node instanceof TypeInsnNode && node.getOpcode().equals(Opcodes.INSTANCEOF) && node.desc.equals("net/minecraft/enchantment/DamageEnchantment")) {
            var nextNode = node.getNext();
            if (nextNode instanceof JumpInsnNode) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(new JumpInsnNode(Opcodes.IFNE, getNthNode(node, 1).label));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/enchantment/ImpalingEnchantment"));
        instructions.insert(node, insnList);
    }
};

var patchInfinityEnchantmentCanApplyTogether = {
    filter: function(node, obfuscated) {
        if (node instanceof TypeInsnNode && node.getOpcode().equals(Opcodes.INSTANCEOF) && node.desc.equals("net/minecraft/enchantment/MendingEnchantment")) {
            var nextNode = node.getNext();
            if (nextNode instanceof JumpInsnNode) {
                return node;
            }
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
        insnList.add(generateHook("canApplyMending", "(Z)Z"));
        instructions.insert(node, insnList);
    }
};

function matchesNode(node, owner, name, desc) {

    return node instanceof MethodInsnNode && node.owner.equals(owner) && node.name.equals(name) && node.desc.equals(desc);
}

function generateHook(name, desc) {

    return new MethodInsnNode(Opcodes.INVOKESTATIC, "com/fuzs/sneakymagic/asm/Hooks", name, desc, false);
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

function log(message) {

    print("[Sneaky Magic Transformer]: " + message);
}