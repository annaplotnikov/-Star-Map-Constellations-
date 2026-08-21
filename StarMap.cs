// StarMap.cs
using System;
using System.Collections.Generic;
using System.Linq;

class Star
{
    public int X { get; set; }
    public int Y { get; set; }
    public string Name { get; set; }
    public double Mag { get; set; }
}

class Constellation
{
    public string Name { get; set; }
    public string Abbr { get; set; }
    public string Hemisphere { get; set; }
    public string Season { get; set; }
    public string BrightestStar { get; set; }
    public string Mythology { get; set; }
    public string NotableObjects { get; set; }
    public List<Star> Stars { get; set; } = new List<Star>();
    public List<int[]> Connections { get; set; } = new List<int[]>();
}

class StarMap
{
    private static readonly List<Constellation> CONSTELLATIONS = new List<Constellation>
    {
        new Constellation
        {
            Name = "Orion", Abbr = "Ori", Hemisphere = "both", Season = "winter",
            BrightestStar = "Betelgeuse (Alpha Orionis, 0.42 mag)",
            Mythology = "Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.",
            NotableObjects = "Orion Nebula (M42), Horsehead Nebula, Barnard's Loop",
            Stars = new List<Star>
            {
                new Star{X=4,Y=1,Name="Betelgeuse",Mag=0.42},
                new Star{X=5,Y=4,Name="Bellatrix",Mag=1.64},
                new Star{X=3,Y=5,Name="Mintaka",Mag=2.23},
                new Star{X=5,Y=5,Name="Alnilam",Mag=1.69},
                new Star{X=7,Y=5,Name="Alnitak",Mag=1.88},
                new Star{X=2,Y=8,Name="Saiph",Mag=2.06},
                new Star{X=6,Y=9,Name="Rigel",Mag=0.12}
            },
            Connections = new List<int[]> { new[]{0,1}, new[]{0,2}, new[]{0,3}, new[]{0,4}, new[]{0,5}, new[]{0,6},
                new[]{1,2}, new[]{2,3}, new[]{3,4}, new[]{4,5}, new[]{5,6}, new[]{2,4} }
        },
        new Constellation
        {
            Name = "Ursa Major", Abbr = "UMa", Hemisphere = "north", Season = "spring",
            BrightestStar = "Alioth (Epsilon Ursae Majoris, 1.77 mag)",
            Mythology = "Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.",
            NotableObjects = "M81, M82 (Bode's Galaxy and Cigar Galaxy), Owl Nebula (M97)",
            Stars = new List<Star>
            {
                new Star{X=1,Y=1,Name="Dubhe",Mag=1.79},
                new Star{X=3,Y=2,Name="Merak",Mag=2.34},
                new Star{X=2,Y=4,Name="Phecda",Mag=2.41},
                new Star{X=4,Y=4,Name="Megrez",Mag=3.31},
                new Star{X=5,Y=5,Name="Alioth",Mag=1.77},
                new Star{X=7,Y=4,Name="Mizar",Mag=2.04},
                new Star{X=9,Y=3,Name="Alkaid",Mag=1.86}
            },
            Connections = new List<int[]> { new[]{0,1}, new[]{1,2}, new[]{2,3}, new[]{3,4}, new[]{4,5}, new[]{5,6} }
        },
        new Constellation
        {
            Name = "Cassiopeia", Abbr = "Cas", Hemisphere = "north", Season = "autumn",
            BrightestStar = "Schedar (Alpha Cassiopeiae, 2.24 mag)",
            Mythology = "Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.",
            NotableObjects = "Cassiopeia A (supernova remnant), NGC 457 (open cluster)",
            Stars = new List<Star>
            {
                new Star{X=2,Y=1,Name="Segin",Mag=3.37},
                new Star{X=3,Y=3,Name="Ruchbah",Mag=2.68},
                new Star{X=5,Y=2,Name="Schedar",Mag=2.24},
                new Star{X=6,Y=4,Name="Navi",Mag=2.47},
                new Star{X=8,Y=3,Name="Caph",Mag=2.28}
            },
            Connections = new List<int[]> { new[]{0,1}, new[]{1,2}, new[]{2,3}, new[]{3,4}, new[]{0,2}, new[]{2,4} }
        },
        new Constellation
        {
            Name = "Scorpius", Abbr = "Sco", Hemisphere = "south", Season = "summer",
            BrightestStar = "Antares (Alpha Scorpii, 0.96 mag)",
            Mythology = "Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.",
            NotableObjects = "Antares (red supergiant), Ptolemy's Cluster (M7), Butterfly Cluster (M6)",
            Stars = new List<Star>
            {
                new Star{X=2,Y=1,Name="Antares",Mag=0.96},
                new Star{X=3,Y=3,Name="Graffias",Mag=2.56},
                new Star{X=5,Y=4,Name="Dschubba",Mag=2.29},
                new Star{X=6,Y=5,Name="Wei",Mag=2.62},
                new Star{X=8,Y=6,Name="Shaula",Mag=1.62}
            },
            Connections = new List<int[]> { new[]{0,1}, new[]{1,2}, new[]{2,3}, new[]{3,4} }
        },
        new Constellation
        {
            Name = "Lyra", Abbr = "Lyr", Hemisphere = "north", Season = "summer",
            BrightestStar = "Vega (Alpha Lyrae, 0.03 mag)",
            Mythology = "Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.",
            NotableObjects = "Ring Nebula (M57), Vega (brightest star)",
            Stars = new List<Star>
            {
                new Star{X=5,Y=1,Name="Vega",Mag=0.03},
                new Star{X=3,Y=4,Name="Sheliak",Mag=3.25},
                new Star{X=7,Y=4,Name="Sulafat",Mag=3.24}
            },
            Connections = new List<int[]> { new[]{0,1}, new[]{0,2}, new[]{1,2} }
        }
    };

    static Constellation GetConstellation(string query)
    {
        var lower = query.ToLower();
        return CONSTELLATIONS.FirstOrDefault(c => c.Name.ToLower() == lower || c.Abbr.ToLower() == lower);
    }

    static List<Constellation> ListAll(string hemisphere)
    {
        return CONSTELLATIONS.Where(c => hemisphere == "all" || c.Hemisphere == hemisphere || c.Hemisphere == "both").ToList();
    }

    static List<Constellation> Search(string term)
    {
        var lower = term.ToLower();
        return CONSTELLATIONS.Where(c =>
            c.Name.ToLower().Contains(lower) ||
            c.Abbr.ToLower().Contains(lower) ||
            c.Mythology.ToLower().Contains(lower)
        ).ToList();
    }

    static List<Constellation> BySeason(string season)
    {
        return CONSTELLATIONS.Where(c => c.Season == season).ToList();
    }

    static void DrawLine(char[][] grid, int x1, int y1, int x2, int y2, char ch)
    {
        int dx = Math.Abs(x2 - x1);
        int dy = Math.Abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        int x = x1, y = y1;
        while (true)
        {
            if (y >= 0 && y < grid.Length && x >= 0 && x < grid[0].Length)
                if (grid[y][x] != '•') grid[y][x] = ch;
            if (x == x2 && y == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 < dx) { err += dx; y += sy; }
        }
    }

    static string DrawMap(string query)
    {
        var c = GetConstellation(query);
        if (c == null) return $"❌ Constellation '{query}' not found.";
        int maxX = 0, maxY = 0;
        foreach (var s in c.Stars)
        {
            if (s.X > maxX) maxX = s.X;
            if (s.Y > maxY) maxY = s.Y;
        }
        maxX += 2; maxY += 2;
        var grid = new char[maxY][];
        for (int i = 0; i < maxY; i++) grid[i] = new string(' ', maxX).ToCharArray();
        foreach (var s in c.Stars) grid[s.Y][s.X] = '•';
        foreach (var conn in c.Connections)
        {
            var s1 = c.Stars[conn[0]];
            var s2 = c.Stars[conn[1]];
            DrawLine(grid, s1.X, s1.Y, s2.X, s2.Y, '·');
        }
        var result = $"\n🌟 Constellation: {c.Name} ({c.Abbr})\n\n";
        foreach (var row in grid) result += "  " + new string(row) + "\n";
        result += "\n";
        foreach (var s in c.Stars) result += $"  {s.Name} (mag {s.Mag:F2})\n";
        return result;
    }

    static string Info(string query)
    {
        var c = GetConstellation(query);
        if (c == null) return $"❌ Constellation '{query}' not found.";
        return $"\n🌟 Constellation: {c.Name}\nAbbreviation: {c.Abbr}\nHemisphere: {c.Hemisphere}\nVisible Season: {char.ToUpper(c.Season[0]) + c.Season.Substring(1)}\nBrightest Star: {c.BrightestStar}\nMythology: {c.Mythology}\nNotable Objects: {c.NotableObjects}";
    }

    static void Main(string[] args)
    {
        if (args.Length < 1)
        {
            Console.WriteLine("Usage: StarMap <command> [options]");
            return;
        }
        var parsed = ParseArgs(args);
        string cmd = args[0];
        switch (cmd)
        {
            case "list":
                string hemisphere = parsed.GetValueOrDefault("hemisphere", "all");
                var results = ListAll(hemisphere);
                Console.WriteLine($"\n📋 Constellations ({results.Count}):");
                foreach (var c in results) Console.WriteLine($"  {c.Name} ({c.Abbr}) – {c.Hemisphere}");
                break;
            case "map":
                if (args.Length < 2) { Console.WriteLine("Error: need a constellation name or abbreviation"); return; }
                Console.Write(DrawMap(args[1]));
                break;
            case "info":
                if (args.Length < 2) { Console.WriteLine("Error: need a constellation name or abbreviation"); return; }
                Console.Write(Info(args[1]));
                break;
            case "season":
                if (args.Length < 2) { Console.WriteLine("Error: need a season (spring, summer, autumn, winter)"); return; }
                var seasonResults = BySeason(args[1]);
                Console.WriteLine($"\n🌿 Constellations visible in {char.ToUpper(args[1][0]) + args[1].Substring(1)}:");
                foreach (var c in seasonResults) Console.WriteLine($"  {c.Name} ({c.Abbr})");
                break;
            case "search":
                if (args.Length < 2) { Console.WriteLine("Error: need a search term"); return; }
                var searchResults = Search(args[1]);
                if (searchResults.Any())
                {
                    Console.WriteLine($"\n🔍 Found {searchResults.Count} constellation(s):");
                    foreach (var c in searchResults)
                        Console.WriteLine($"  {c.Name} ({c.Abbr}) – {(c.Mythology.Length > 60 ? c.Mythology.Substring(0,60) + "..." : c.Mythology)}");
                }
                else Console.WriteLine("❌ No matches found.");
                break;
            default:
                Console.WriteLine("Unknown command. Use list, map, info, season, search.");
                break;
        }
    }

    static Dictionary<string, string> ParseArgs(string[] args)
    {
        var dict = new Dictionary<string, string>();
        for (int i = 1; i < args.Length; i++)
        {
            if (args[i].StartsWith("--") && i + 1 < args.Length)
                dict[args[i].Substring(2)] = args[++i];
        }
        return dict;
    }
}
