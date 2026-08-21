# star_map.py
import sys
import json
import argparse
from typing import List, Dict, Optional, Tuple
import math

# Constellation data: name, abbreviation, hemisphere, season, brightest star, mythology, notable objects, star positions for map
CONSTELLATIONS = [
    {
        "name": "Orion",
        "abbr": "Ori",
        "hemisphere": "both",
        "season": "winter",
        "brightest_star": "Betelgeuse (Alpha Orionis, 0.42 mag)",
        "mythology": "Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.",
        "notable_objects": "Orion Nebula (M42), Horsehead Nebula, Barnard's Loop",
        "stars": [
            {"x": 4, "y": 1, "name": "Betelgeuse", "mag": 0.42},
            {"x": 5, "y": 4, "name": "Bellatrix", "mag": 1.64},
            {"x": 3, "y": 5, "name": "Mintaka", "mag": 2.23},
            {"x": 5, "y": 5, "name": "Alnilam", "mag": 1.69},
            {"x": 7, "y": 5, "name": "Alnitak", "mag": 1.88},
            {"x": 2, "y": 8, "name": "Saiph", "mag": 2.06},
            {"x": 6, "y": 9, "name": "Rigel", "mag": 0.12},
        ],
        "connections": [(0,1), (0,2), (0,3), (0,4), (0,5), (0,6), 
                        (1,2), (2,3), (3,4), (4,5), (5,6), (2,4)]
    },
    {
        "name": "Ursa Major",
        "abbr": "UMa",
        "hemisphere": "north",
        "season": "spring",
        "brightest_star": "Alioth (Epsilon Ursae Majoris, 1.77 mag)",
        "mythology": "Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.",
        "notable_objects": "M81, M82 (Bode's Galaxy and Cigar Galaxy), Owl Nebula (M97)",
        "stars": [
            {"x": 1, "y": 1, "name": "Dubhe", "mag": 1.79},
            {"x": 3, "y": 2, "name": "Merak", "mag": 2.34},
            {"x": 2, "y": 4, "name": "Phecda", "mag": 2.41},
            {"x": 4, "y": 4, "name": "Megrez", "mag": 3.31},
            {"x": 5, "y": 5, "name": "Alioth", "mag": 1.77},
            {"x": 7, "y": 4, "name": "Mizar", "mag": 2.04},
            {"x": 9, "y": 3, "name": "Alkaid", "mag": 1.86},
        ],
        "connections": [(0,1), (1,2), (2,3), (3,4), (4,5), (5,6)]
    },
    {
        "name": "Cassiopedia",
        "abbr": "Cas",
        "hemisphere": "north",
        "season": "autumn",
        "brightest_star": "Schedar (Alpha Cassiopeiae, 2.24 mag)",
        "mythology": "Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.",
        "notable_objects": "Cassiopeia A (supernova remnant), NGC 457 (open cluster)",
        "stars": [
            {"x": 2, "y": 1, "name": "Segin", "mag": 3.37},
            {"x": 3, "y": 3, "name": "Ruchbah", "mag": 2.68},
            {"x": 5, "y": 2, "name": "Schedar", "mag": 2.24},
            {"x": 6, "y": 4, "name": "Navi", "mag": 2.47},
            {"x": 8, "y": 3, "name": "Caph", "mag": 2.28},
        ],
        "connections": [(0,1), (1,2), (2,3), (3,4), (0,2), (2,4)]
    },
    {
        "name": "Scorpius",
        "abbr": "Sco",
        "hemisphere": "south",
        "season": "summer",
        "brightest_star": "Antares (Alpha Scorpii, 0.96 mag)",
        "mythology": "Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.",
        "notable_objects": "Antares (red supergiant), Ptolemy's Cluster (M7), Butterfly Cluster (M6)",
        "stars": [
            {"x": 2, "y": 1, "name": "Antares", "mag": 0.96},
            {"x": 3, "y": 3, "name": "Graffias", "mag": 2.56},
            {"x": 5, "y": 4, "name": "Dschubba", "mag": 2.29},
            {"x": 6, "y": 5, "name": "Wei", "mag": 2.62},
            {"x": 8, "y": 6, "name": "Shaula", "mag": 1.62},
        ],
        "connections": [(0,1), (1,2), (2,3), (3,4)]
    },
    {
        "name": "Lyra",
        "abbr": "Lyr",
        "hemisphere": "north",
        "season": "summer",
        "brightest_star": "Vega (Alpha Lyrae, 0.03 mag)",
        "mythology": "Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.",
        "notable_objects": "Ring Nebula (M57), Vega (brightest star)",
        "stars": [
            {"x": 5, "y": 1, "name": "Vega", "mag": 0.03},
            {"x": 3, "y": 4, "name": "Sheliak", "mag": 3.25},
            {"x": 7, "y": 4, "name": "Sulafat", "mag": 3.24},
        ],
        "connections": [(0,1), (0,2), (1,2)]
    },
]

class StarMap:
    def __init__(self):
        self.data = CONSTELLATIONS

    def get_constellation(self, query: str) -> Optional[Dict]:
        query_lower = query.lower()
        for c in self.data:
            if c["name"].lower() == query_lower or c["abbr"].lower() == query_lower:
                return c
        return None

    def list_all(self, hemisphere: Optional[str] = None) -> List[Dict]:
        if hemisphere:
            return [c for c in self.data if c["hemisphere"] == hemisphere or c["hemisphere"] == "both"]
        return self.data

    def search(self, term: str) -> List[Dict]:
        term_lower = term.lower()
        results = []
        for c in self.data:
            if (term_lower in c["name"].lower() or
                term_lower in c["abbr"].lower() or
                term_lower in c["mythology"].lower()):
                results.append(c)
        return results

    def by_season(self, season: str) -> List[Dict]:
        return [c for c in self.data if c["season"] == season.lower()]

    def draw_map(self, name: str) -> str:
        c = self.get_constellation(name)
        if not c:
            return f"❌ Constellation '{name}' not found."
        stars = c["stars"]
        connections = c.get("connections", [])
        # Determine grid size
        max_x = max(s["x"] for s in stars) + 2
        max_y = max(s["y"] for s in stars) + 2
        grid = [[" " for _ in range(max_x)] for _ in range(max_y)]
        # Place stars
        for s in stars:
            grid[s["y"]][s["x"]] = "•"
        # Draw connections
        for a, b in connections:
            x1, y1 = stars[a]["x"], stars[a]["y"]
            x2, y2 = stars[b]["x"], stars[b]["y"]
            self._draw_line(grid, x1, y1, x2, y2, "·")
        # Add star names
        result = f"\n🌟 Constellation: {c['name']} ({c['abbr']})\n\n"
        for row in grid:
            result += "  " + "".join(row) + "\n"
        result += "\n"
        # Add star info
        for s in stars:
            result += f"  {s['name']} (mag {s['mag']:.2f})\n"
        return result

    def _draw_line(self, grid, x1, y1, x2, y2, char):
        dx = abs(x2 - x1)
        dy = abs(y2 - y1)
        sx = 1 if x1 < x2 else -1
        sy = 1 if y1 < y2 else -1
        err = dx - dy
        x, y = x1, y1
        while True:
            if 0 <= y < len(grid) and 0 <= x < len(grid[0]):
                if grid[y][x] != "•":
                    grid[y][x] = char
            if x == x2 and y == y2:
                break
            e2 = 2 * err
            if e2 > -dy:
                err -= dy
                x += sx
            if e2 < dx:
                err += dx
                y += sy

    def info(self, query: str) -> str:
        c = self.get_constellation(query)
        if not c:
            return f"❌ Constellation '{query}' not found."
        result = f"\n🌟 Constellation: {c['name']}\n"
        result += f"Abbreviation: {c['abbr']}\n"
        result += f"Hemisphere: {c['hemisphere']}\n"
        result += f"Visible Season: {c['season'].capitalize()}\n"
        result += f"Brightest Star: {c['brightest_star']}\n"
        result += f"Mythology: {c['mythology']}\n"
        result += f"Notable Objects: {c['notable_objects']}\n"
        return result

def main():
    parser = argparse.ArgumentParser(description="Star Map - Constellations")
    subparsers = parser.add_subparsers(dest="cmd", required=True)

    list_parser = subparsers.add_parser("list")
    list_parser.add_argument("--hemisphere", choices=["north", "south", "all"], default="all")

    map_parser = subparsers.add_parser("map")
    map_parser.add_argument("query", help="Constellation name or abbreviation")

    info_parser = subparsers.add_parser("info")
    info_parser.add_argument("query", help="Constellation name or abbreviation")

    season_parser = subparsers.add_parser("season")
    season_parser.add_argument("season", choices=["spring", "summer", "autumn", "winter"])

    search_parser = subparsers.add_parser("search")
    search_parser.add_argument("term", help="Search term")

    args = parser.parse_args()
    app = StarMap()

    if args.cmd == "list":
        results = app.list_all(None if args.hemisphere == "all" else args.hemisphere)
        print(f"\n📋 Constellations ({len(results)}):")
        for c in results:
            print(f"  {c['name']} ({c['abbr']}) – {c['hemisphere']}")

    elif args.cmd == "map":
        print(app.draw_map(args.query))

    elif args.cmd == "info":
        print(app.info(args.query))

    elif args.cmd == "season":
        results = app.by_season(args.season)
        print(f"\n🌿 Constellations visible in {args.season.capitalize()}:")
        for c in results:
            print(f"  {c['name']} ({c['abbr']})")

    elif args.cmd == "search":
        results = app.search(args.term)
        if results:
            print(f"\n🔍 Found {len(results)} constellation(s):")
            for c in results:
                print(f"  {c['name']} ({c['abbr']}) – {c['mythology'][:60]}...")
        else:
            print("❌ No matches found.")

if __name__ == "__main__":
    main()
