package com.github.pinont.devtool.utils;

import com.github.pinont.singularitylib.api.items.ItemCreator;
import com.github.pinont.singularitylib.api.ui.Button;
import com.github.pinont.singularitylib.api.ui.Layout;
import com.github.pinont.singularitylib.api.ui.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import static com.github.pinont.singularitylib.plugin.CorePlugin.getInstance;

/**
 * Creates and returns the world creator menu with specified parameters.
 */
public class getWorldCreatorMenu {

    public static Menu getWorldCreatorMenu(String name, World.Environment environment, WorldType worldType, boolean generate_structure, int borderSize, Difficulty difficulty, Long seed) {
        Menu worldCreatorMenu = new Menu("World Creator").setLayout("----w----", "-=n=e=t=-", "-=g=b=s=-", "----c----");
        return worldCreatorMenu.setKey(
                blank.blank(),
                new Layout() {
                    @Override
                    public char getKey() {
                        return '-';
                    } // border

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").create();
                            }

                            @Override
                            public void onClick(Player player) {

                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'w';
                    } // world creator icon

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(new ItemStack(Material.GOLD_BLOCK)).setName(ChatColor.BOLD + "" + ChatColor.YELLOW + "World Creator").addLore(
                                        ChatColor.GRAY + "Name: " + ChatColor.YELLOW + (name == null ? ChatColor.RED + "Not Set" : name),
                                        ChatColor.GRAY + "Environment Type: " + ChatColor.YELLOW + (environment == null ? ChatColor.RED + "Not Set" : environment),
                                        ChatColor.GRAY + "World Type: " + ChatColor.YELLOW + (worldType == null ? ChatColor.RED + "Not Set" : worldType),
                                        ChatColor.GRAY + "Difficulty: " + ChatColor.YELLOW + difficulty,
                                        ChatColor.GRAY + "Generate Structure: " + ChatColor.YELLOW + (generate_structure ? "True" : "False"),
                                        ChatColor.GRAY + "Border Size: " + ChatColor.YELLOW + borderSize,
                                        ChatColor.GRAY + "Seed: " + ChatColor.YELLOW + (seed == null ? ChatColor.RED + "RANDOM" : seed)
                                ).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                player.closeInventory();
                                player.sendMessage(ChatColor.YELLOW + "World '" + name + "' is Creating...");
                                createWorld.createWorld(name, environment, worldType, generate_structure, borderSize, difficulty, seed);
                                player.sendMessage(ChatColor.GREEN + "World '" + name + "' has been created!");
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'n';
                    } // set world name

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                if (name == null) {
                                    return new ItemCreator(Material.OAK_SIGN).setName("Set World Name").addLore(ChatColor.YELLOW + "Click to set world name.").create();
                                }
                                return new ItemCreator(Material.BIRCH_SIGN).setName(name).addLore(ChatColor.YELLOW + "Click to change world name.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                player.sendMessage(ChatColor.GRAY + "Please send a world name into chat.");
                                player.setMetadata("devTool", new FixedMetadataValue(getInstance(), new WorldCreatorContent() {
                                    @Override
                                    public String getInputContent() {
                                        return "worldName";
                                    }

                                    @Override
                                    public String getWorldName() {
                                        return name;
                                    }

                                    @Override
                                    public World.Environment getEnvironment() {
                                        return environment;
                                    }

                                    @Override
                                    public WorldType getWorldType() {
                                        return worldType;
                                    }

                                    @Override
                                    public boolean getGenerateStructure() {
                                        return generate_structure;
                                    }

                                    @Override
                                    public int getBorderSize() {
                                        return borderSize;
                                    }

                                    @Override
                                    public Difficulty getDifficulty() {
                                        return difficulty;
                                    }

                                    @Override
                                    public Long getSeed() {
                                        return seed;
                                    }
                                }));
                                player.closeInventory();
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'e'; // environment
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                if (environment == null) {
                                    return new ItemCreator(Material.COMMAND_BLOCK).setName("Set World Environment").addLore(ChatColor.YELLOW + "Click to change world environment.").create();
                                }
                                return new ItemCreator(getWorldEnvironmentBlock.getWorldEnvironmentBlock(environment)).setName(environment.name()).addLore(ChatColor.YELLOW + "Click to change world environment.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                World.Environment[] environments = World.Environment.values();
                                int currentIndex = environment == null ? -1 : java.util.Arrays.asList(environments).indexOf(environment);
                                int nextIndex = (currentIndex + 1) % environments.length;
                                showWorldCreator.showWorldCreator(player, name, environments[nextIndex], worldType, generate_structure, borderSize, difficulty, seed);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 't'; // world type
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                if (worldType == null) {
                                    return new ItemCreator(Material.OAK_SAPLING).setName("Set World Type").addLore(ChatColor.YELLOW + "Click to change world type.").create();
                                }
                                return new ItemCreator(Material.CHERRY_SAPLING).setName(worldType.getName()).addLore(ChatColor.YELLOW + "Click to change world type").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                WorldType[] worldTypes = WorldType.values();
                                int currentTypeIndex = worldType == null ? -1 : java.util.Arrays.asList(worldTypes).indexOf(worldType);
                                int nextTypeIndex = (currentTypeIndex + 1) % worldTypes.length;

                                showWorldCreator.showWorldCreator(player, name, environment, worldTypes[nextTypeIndex], generate_structure, borderSize, difficulty, seed);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'g'; // generate structure? def = true
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                if (generate_structure) return new ItemCreator(Material.BIRCH_STAIRS).setName(ChatColor.GRAY + "Generate Structure: " + ChatColor.GREEN + "True").create();
                                return new ItemCreator(Material.ACACIA_STAIRS).setName(ChatColor.GRAY + "Generate Structure: " + ChatColor.GREEN + "False").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                showWorldCreator.showWorldCreator(player, name, environment, worldType, !generate_structure, borderSize, difficulty, seed);
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'b'; // border size? def = default
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.STRUCTURE_VOID).setName(ChatColor.GRAY + "World Border Size: " + ChatColor.YELLOW + borderSize).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                player.sendMessage(ChatColor.GRAY + "Please send a world border size into chat.");
                                player.setMetadata("devTool", new FixedMetadataValue(getInstance(), new WorldCreatorContent() {
                                    @Override
                                    public String getInputContent() {
                                        return "worldBorder";
                                    }

                                    @Override
                                    public String getWorldName() {
                                        return name;
                                    }

                                    @Override
                                    public World.Environment getEnvironment() {
                                        return environment;
                                    }

                                    @Override
                                    public WorldType getWorldType() {
                                        return worldType;
                                    }

                                    @Override
                                    public boolean getGenerateStructure() {
                                        return generate_structure;
                                    }

                                    @Override
                                    public int getBorderSize() {
                                        return borderSize;
                                    }

                                    @Override
                                    public Difficulty getDifficulty() {
                                        return difficulty;
                                    }

                                    @Override
                                    public Long getSeed() {
                                        return seed;
                                    }
                                }));
                                player.closeInventory();
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 's'; // seed? def = default
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.WHEAT_SEEDS).setName(ChatColor.GRAY + "Seed: " + ChatColor.YELLOW + seed).create();
                            }

                            @Override
                            public void onClick(Player player) {
                                player.sendMessage(ChatColor.GRAY + "Please send a seed number into chat.");
                                player.setMetadata("devTool", new FixedMetadataValue(getInstance(), new WorldCreatorContent() {
                                    @Override
                                    public String getInputContent() {
                                        return "worldSeed";
                                    }

                                    @Override
                                    public String getWorldName() {
                                        return name;
                                    }

                                    @Override
                                    public World.Environment getEnvironment() {
                                        return environment;
                                    }

                                    @Override
                                    public WorldType getWorldType() {
                                        return worldType;
                                    }

                                    @Override
                                    public boolean getGenerateStructure() {
                                        return generate_structure;
                                    }

                                    @Override
                                    public int getBorderSize() {
                                        return borderSize;
                                    }

                                    @Override
                                    public Difficulty getDifficulty() {
                                        return difficulty;
                                    }

                                    @Override
                                    public Long getSeed() {
                                        return seed;
                                    }
                                }));
                                player.closeInventory();
                            }
                        };
                    }
                },
                new Layout() {
                    @Override
                    public char getKey() {
                        return 'c'; // create button
                    }

                    @Override
                    public Button getButton() {
                        return new Button() {
                            @Override
                            public ItemStack getItem() {
                                return new ItemCreator(Material.DIAMOND).setName(ChatColor.WHITE + "Create World").addLore(ChatColor.YELLOW + "Click to create world.").create();
                            }

                            @Override
                            public void onClick(Player player) {
                                player.closeInventory();
                                player.sendMessage(ChatColor.YELLOW + "World '" + name + "' is Creating...");
                                createWorld.createWorld(name, environment, worldType, generate_structure, borderSize, difficulty, seed);
                                player.sendMessage(ChatColor.GREEN + "World '" + name + "' has been created!");
                            }
                        };
                    }
                }
        );
    }
}
