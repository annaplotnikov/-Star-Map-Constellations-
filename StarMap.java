// StarMap.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.google.gson.*;

class Star {
    int x, y;
    String name;
    double mag;
}

class Constellation {
    String name, abbr, hemisphere, season, brightest_star, mythology, notable_objects;
    List<Star> stars = new ArrayList<>();
    List<List<Integer>> connections = new ArrayList<>();
}

public class StarMap {
    private static List<Constellation> constellations = new ArrayList<>();
    private static final Gson gson = new Gson();

    static {
        // We'll hardcode a few constellations for brevity
        Constellation orion = new Constellation();
        orion.name = "Orion"; orion.abbr = "Ori"; orion.hemisphere = "both"; orion.season = "winter";
        orion.brightest_star = "Betelgeuse (Alpha Orionis, 0.42 mag)";
        orion.mythology = "Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.";
        orion.notable_objects = "Orion Nebula (M42), Horsehead Nebula, Barnard's Loop";
        orion.stars = Arrays.asList(
            new Star(){{x=4;y=1;name="Betelgeuse";mag=0.42;}},
            new Star(){{x=5;y=4;name="Bellatrix";mag=1.64;}},
            new Star(){{x=3;y=5;name="Mintaka";mag=2.23;}},
            new Star(){{x=5;y=5;name="Alnilam";mag=1.69;}},
            new Star(){{x=7;y=5;name="Alnitak";mag=1.88;}},
            new Star(){{x=2;y=8;name="Saiph";mag=2.06;}},
            new Star(){{x=6;y=9;name="Rigel";mag=0.12;}}
        );
        orion.connections = Arrays.asList(
            Arrays.asList(0,1), Arrays.asList(0,2), Arrays.asList(0,3), Arrays.asList(0,4),
            Arrays.asList(0,5), Arrays.asList(0,6), Arrays.asList(1,2), Arrays.asList(2,3),
            Arrays.asList(3,4), Arrays.asList(4,5), Arrays.asList(5,6), Arrays.asList(2,4)
        );
        constellations.add(orion);

        Constellation ursa = new Constellation();
        ursa.name = "Ursa Major"; ursa.abbr = "UMa"; ursa.hemisphere = "north"; ursa.season = "spring";
        ursa.brightest_star = "Alioth (Epsilon Ursae Majoris, 1.77 mag)";
        ursa.mythology = "Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.";
        ursa.notable_objects = "M81, M82 (Bode's Galaxy and Cigar Galaxy), Owl Nebula (M97)";
        ursa.stars = Arrays.asList(
            new Star(){{x=1;y=1;name="Dubhe";mag=1.79;}},
            new Star(){{x=3;y=2;name="Merak";mag=2.34;}},
            new Star(){{x=2;y=4;name="Phecda";mag=2.41;}},
            new Star(){{x=4;y=4;name="Megrez";mag=3.31;}},
            new Star(){{x=5;y=5;name="Alioth";mag=1.77;}},
            new Star(){{x=7;y=4;name="Mizar";mag=2.04;}},
            new Star(){{x=9;y=3;name="Alkaid";mag=1.86;}}
        );
        ursa.connections = Arrays.asList(
            Arrays.asList(0,1), Arrays.asList(1,2), Arrays.asList(2,3),
            Arrays.asList(3,4), Arrays.asList(4,5), Arrays.asList(5,6)
        );
        constellations.add(ursa);

        Constellation cass = new Constellation();
        cass.name = "Cassiopeia"; cass.abbr = "Cas"; cass.hemisphere = "north"; cass.season = "autumn";
        cass.brightest_star = "Schedar (Alpha Cassiopeiae, 2.24 mag)";
        cass.mythology = "Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.";
        cass.notable_objects = "Cassiopeia A (supernova remnant), NGC 457 (open cluster)";
        cass.stars = Arrays.asList(
            new Star(){{x=2;y=1;name="Segin";mag=3.37;}},
            new Star(){{x=3;y=3;name="Ruchbah";mag=2.68;}},
            new Star(){{x=5;y=2;name="Schedar";mag=2.24;}},
            new Star(){{x=6;y=4;name="Navi";mag=2.47;}},
            new Star(){{x=8;y=3;name="Caph";mag=2.28;}}
        );
        cass.connections = Arrays.asList(
            Arrays.asList(0,1), Arrays.asList(1,2), Arrays.asList(2,3),
            Arrays.asList(3,4), Arrays.asList(0,2), Arrays.asList(2,4)
        );
        constellations.add(cass);

        Constellation scorp = new Constellation();
        scorp.name = "Scorpius"; scorp.abbr = "Sco"; scorp.hemisphere = "south"; scorp.season = "summer";
        scorp.brightest_star = "Antares (Alpha Scorpii, 0.96 mag)";
        scorp.mythology = "Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.";
        scorp.notable_objects = "Antares (red supergiant), Ptolemy's Cluster (M7), Butterfly Cluster (M6)";
        scorp.stars = Arrays.asList(
            new Star(){{x=2;y=1;name="Antares";mag=0.96;}},
            new Star(){{x=3;y=3;name="Graffias";mag=2.56;}},
            new Star(){{x=5;y=4;name="Dschubba";mag=2.29;}},
            new Star(){{x=6;y=5;name="Wei";mag=2.62;}},
            new Star(){{x=8;y=6;name="Shaula";mag=1.62;}}
        );
        scorp.connections = Arrays.asList(
            Arrays.asList(0,1), Arrays.asList(1,2), Arrays.asList(2,3), Arrays.asList(3,4)
        );
        constellations.add(scorp);

        Constellation lyra = new Constellation();
        lyra.name = "Lyra"; lyra.abbr = "Lyr"; lyra.hemisphere = "north"; lyra.season = "summer";
        lyra.brightest_star = "Vega (Alpha Lyrae, 0.03 mag)";
        lyra.mythology = "Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.";
        lyra.notable_objects = "Ring Nebula (M57), Vega (brightest star)";
        lyra.stars = Arrays.asList(
            new Star(){{x=5;y=1;name="Vega";mag=0.03;}},
            new Star(){{x=3;y=4;name="Sheliak";mag=3.25;}},
            new Star(){{x=7;y=4;name="Sulafat";mag=3.24;}}
        );
        lyra.connections = Arrays.asList(
            Arrays.asList(0,1), Arrays.asList(0,2), Arrays.asList(1,2)
        );
        constellations.add(lyra);
    }

    public static Constellation getConstellation(String query) {
        String lower = query.toLowerCase();
        for (Constellation c : constellations) {
            if (c.name.toLowerCase().equals(lower) || c.abbr.toLowerCase().equals(lower))
                return c;
        }
        return null;
    }

    public static List<Constellation> listAll(String hemisphere) {
        List<Constellation> result = new ArrayList<>();
        for (Constellation c : constellations) {
            if (hemisphere.equals("all") || c.hemisphere.equals(hemisphere) || c.hemisphere.equals("both")) {
                result.add(c);
            }
        }
        return result;
    }

    public static List<Constellation> search(String term) {
        String lower = term.toLowerCase();
        List<Constellation> result = new ArrayList<>();
        for (Constellation c : constellations) {
            if (c.name.toLowerCase().contains(lower) || c.abbr.toLowerCase().contains(lower) ||
                c.mythology.toLowerCase().contains(lower)) {
                result.add(c);
            }
        }
        return result;
    }

    public static List<Constellation> bySeason(String season) {
        List<Constellation> result = new ArrayList<>();
        for (Constellation c : constellations) {
            if (c.season.equals(season)) result.add(c);
        }
        return result;
    }

    public static void drawLine(char[][] grid, int x1, int y1, int x2, int y2, char ch) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        int x = x1, y = y1;
        while (true) {
            if (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
                if (grid[y][x] != '•') grid[y][x] = ch;
            }
            if (x == x2 && y == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 < dx) { err += dx; y += sy; }
        }
    }

    public static String drawMap(String query) {
        Constellation c = getConstellation(query);
        if (c == null) return "❌ Constellation '" + query + "' not found.";
        int maxX = 0, maxY = 0;
        for (Star s : c.stars) {
            if (s.x > maxX) maxX = s.x;
            if (s.y > maxY) maxY = s.y;
        }
        maxX += 2; maxY += 2;
        char[][] grid = new char[maxY][maxX];
        for (int i = 0; i < maxY; i++) Arrays.fill(grid[i], ' ');
        for (Star s : c.stars) grid[s.y][s.x] = '•';
        for (List<Integer> conn : c.connections) {
            Star s1 = c.stars.get(conn.get(0));
            Star s2 = c.stars.get(conn.get(1));
            drawLine(grid, s1.x, s1.y, s2.x, s2.y, '·');
        }
        StringBuilder result = new StringBuilder();
        result.append("\n🌟 Constellation: ").append(c.name).append(" (").append(c.abbr).append(")\n\n");
        for (char[] row : grid) {
            result.append("  ").append(new String(row)).append("\n");
        }
        result.append("\n");
        for (Star s : c.stars) {
            result.append(String.format("  %s (mag %.2f)\n", s.name, s.mag));
        }
        return result.toString();
    }

    public static String info(String query) {
        Constellation c = getConstellation(query);
        if (c == null) return "❌ Constellation '" + query + "' not found.";
        return String.format("\n🌟 Constellation: %s\nAbbreviation: %s\nHemisphere: %s\nVisible Season: %s\nBrightest Star: %s\nMythology: %s\nNotable Objects: %s\n",
            c.name, c.abbr, c.hemisphere, c.season.substring(0,1).toUpperCase() + c.season.substring(1),
            c.brightest_star, c.mythology, c.notable_objects);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: StarMap <command> [options]");
            return;
        }
        String cmd = args[0];
        Map<String, String> params = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--") && i+1 < args.length) {
                params.put(args[i].substring(2), args[++i]);
            }
        }
        switch (cmd) {
            case "list": {
                String hemisphere = params.getOrDefault("hemisphere", "all");
                List<Constellation> results = listAll(hemisphere);
                System.out.printf("\n📋 Constellations (%d):\n", results.size());
                for (Constellation c : results) {
                    System.out.printf("  %s (%s) – %s\n", c.name, c.abbr, c.hemisphere);
                }
                break;
            }
            case "map": {
                if (args.length < 2) { System.out.println("Error: need a constellation name or abbreviation"); return; }
                System.out.print(drawMap(args[1]));
                break;
            }
            case "info": {
                if (args.length < 2) { System.out.println("Error: need a constellation name or abbreviation"); return; }
                System.out.print(info(args[1]));
                break;
            }
            case "season": {
                if (args.length < 2) { System.out.println("Error: need a season (spring, summer, autumn, winter)"); return; }
                List<Constellation> results = bySeason(args[1]);
                System.out.printf("\n🌿 Constellations visible in %s:\n", args[1].substring(0,1).toUpperCase() + args[1].substring(1));
                for (Constellation c : results) {
                    System.out.printf("  %s (%s)\n", c.name, c.abbr);
                }
                break;
            }
            case "search": {
                if (args.length < 2) { System.out.println("Error: need a search term"); return; }
                List<Constellation> results = search(args[1]);
                if (!results.isEmpty()) {
                    System.out.printf("\n🔍 Found %d constellation(s):\n", results.size());
                    for (Constellation c : results) {
                        String myth = c.mythology.length() > 60 ? c.mythology.substring(0,60) + "..." : c.mythology;
                        System.out.printf("  %s (%s) – %s\n", c.name, c.abbr, myth);
                    }
                } else {
                    System.out.println("❌ No matches found.");
                }
                break;
            }
            default:
                System.out.println("Unknown command. Use list, map, info, season, search.");
        }
    }
}
