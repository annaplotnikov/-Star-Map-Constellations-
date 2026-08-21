// star_map.go
package main

import (
	"flag"
	"fmt"
	"math"
	"os"
	"strings"
)

type Star struct {
	X    int     `json:"x"`
	Y    int     `json:"y"`
	Name string  `json:"name"`
	Mag  float64 `json:"mag"`
}

type Constellation struct {
	Name          string   `json:"name"`
	Abbr          string   `json:"abbr"`
	Hemisphere    string   `json:"hemisphere"`
	Season        string   `json:"season"`
	BrightestStar string   `json:"brightest_star"`
	Mythology     string   `json:"mythology"`
	NotableObjs   string   `json:"notable_objects"`
	Stars         []Star   `json:"stars"`
	Connections   [][2]int `json:"connections"`
}

var constellations = []Constellation{
	{
		Name:          "Orion",
		Abbr:          "Ori",
		Hemisphere:    "both",
		Season:        "winter",
		BrightestStar: "Betelgeuse (Alpha Orionis, 0.42 mag)",
		Mythology:     "Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.",
		NotableObjs:   "Orion Nebula (M42), Horsehead Nebula, Barnard's Loop",
		Stars: []Star{
			{4, 1, "Betelgeuse", 0.42},
			{5, 4, "Bellatrix", 1.64},
			{3, 5, "Mintaka", 2.23},
			{5, 5, "Alnilam", 1.69},
			{7, 5, "Alnitak", 1.88},
			{2, 8, "Saiph", 2.06},
			{6, 9, "Rigel", 0.12},
		},
		Connections: [][2]int{{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {2, 4}},
	},
	{
		Name:          "Ursa Major",
		Abbr:          "UMa",
		Hemisphere:    "north",
		Season:        "spring",
		BrightestStar: "Alioth (Epsilon Ursae Majoris, 1.77 mag)",
		Mythology:     "Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.",
		NotableObjs:   "M81, M82 (Bode's Galaxy and Cigar Galaxy), Owl Nebula (M97)",
		Stars: []Star{
			{1, 1, "Dubhe", 1.79},
			{3, 2, "Merak", 2.34},
			{2, 4, "Phecda", 2.41},
			{4, 4, "Megrez", 3.31},
			{5, 5, "Alioth", 1.77},
			{7, 4, "Mizar", 2.04},
			{9, 3, "Alkaid", 1.86},
		},
		Connections: [][2]int{{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}},
	},
	{
		Name:          "Cassiopeia",
		Abbr:          "Cas",
		Hemisphere:    "north",
		Season:        "autumn",
		BrightestStar: "Schedar (Alpha Cassiopeiae, 2.24 mag)",
		Mythology:     "Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.",
		NotableObjs:   "Cassiopeia A (supernova remnant), NGC 457 (open cluster)",
		Stars: []Star{
			{2, 1, "Segin", 3.37},
			{3, 3, "Ruchbah", 2.68},
			{5, 2, "Schedar", 2.24},
			{6, 4, "Navi", 2.47},
			{8, 3, "Caph", 2.28},
		},
		Connections: [][2]int{{0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 2}, {2, 4}},
	},
	{
		Name:          "Scorpius",
		Abbr:          "Sco",
		Hemisphere:    "south",
		Season:        "summer",
		BrightestStar: "Antares (Alpha Scorpii, 0.96 mag)",
		Mythology:     "Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.",
		NotableObjs:   "Antares (red supergiant), Ptolemy's Cluster (M7), Butterfly Cluster (M6)",
		Stars: []Star{
			{2, 1, "Antares", 0.96},
			{3, 3, "Graffias", 2.56},
			{5, 4, "Dschubba", 2.29},
			{6, 5, "Wei", 2.62},
			{8, 6, "Shaula", 1.62},
		},
		Connections: [][2]int{{0, 1}, {1, 2}, {2, 3}, {3, 4}},
	},
	{
		Name:          "Lyra",
		Abbr:          "Lyr",
		Hemisphere:    "north",
		Season:        "summer",
		BrightestStar: "Vega (Alpha Lyrae, 0.03 mag)",
		Mythology:     "Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.",
		NotableObjs:   "Ring Nebula (M57), Vega (brightest star)",
		Stars: []Star{
			{5, 1, "Vega", 0.03},
			{3, 4, "Sheliak", 3.25},
			{7, 4, "Sulafat", 3.24},
		},
		Connections: [][2]int{{0, 1}, {0, 2}, {1, 2}},
	},
}

func getConstellation(query string) *Constellation {
	lower := strings.ToLower(query)
	for i := range constellations {
		if strings.ToLower(constellations[i].Name) == lower || strings.ToLower(constellations[i].Abbr) == lower {
			return &constellations[i]
		}
	}
	return nil
}

func listAll(hemisphere string) []Constellation {
	var result []Constellation
	for _, c := range constellations {
		if hemisphere == "all" || c.Hemisphere == hemisphere || c.Hemisphere == "both" {
			result = append(result, c)
		}
	}
	return result
}

func search(term string) []Constellation {
	var result []Constellation
	lower := strings.ToLower(term)
	for _, c := range constellations {
		if strings.Contains(strings.ToLower(c.Name), lower) ||
			strings.Contains(strings.ToLower(c.Abbr), lower) ||
			strings.Contains(strings.ToLower(c.Mythology), lower) {
			result = append(result, c)
		}
	}
	return result
}

func bySeason(season string) []Constellation {
	var result []Constellation
	for _, c := range constellations {
		if c.Season == season {
			result = append(result, c)
		}
	}
	return result
}

func drawLine(grid [][]string, x1, y1, x2, y2 int, char string) {
	dx := abs(x2 - x1)
	dy := abs(y2 - y1)
	sx := 1
	if x1 > x2 {
		sx = -1
	}
	sy := 1
	if y1 > y2 {
		sy = -1
	}
	err := dx - dy
	x, y := x1, y1
	for {
		if y >= 0 && y < len(grid) && x >= 0 && x < len(grid[0]) {
			if grid[y][x] != "•" {
				grid[y][x] = char
			}
		}
		if x == x2 && y == y2 {
			break
		}
		e2 := 2 * err
		if e2 > -dy {
			err -= dy
			x += sx
		}
		if e2 < dx {
			err += dx
			y += sy
		}
	}
}

func drawMap(query string) string {
	c := getConstellation(query)
	if c == nil {
		return fmt.Sprintf("❌ Constellation '%s' not found.", query)
	}
	maxX, maxY := 0, 0
	for _, s := range c.Stars {
		if s.X > maxX {
			maxX = s.X
		}
		if s.Y > maxY {
			maxY = s.Y
		}
	}
	maxX += 2
	maxY += 2
	grid := make([][]string, maxY)
	for i := range grid {
		grid[i] = make([]string, maxX)
		for j := range grid[i] {
			grid[i][j] = " "
		}
	}
	for _, s := range c.Stars {
		grid[s.Y][s.X] = "•"
	}
	for _, conn := range c.Connections {
		s1 := c.Stars[conn[0]]
		s2 := c.Stars[conn[1]]
		drawLine(grid, s1.X, s1.Y, s2.X, s2.Y, "·")
	}
	result := fmt.Sprintf("\n🌟 Constellation: %s (%s)\n\n", c.Name, c.Abbr)
	for _, row := range grid {
		result += "  " + strings.Join(row, "") + "\n"
	}
	result += "\n"
	for _, s := range c.Stars {
		result += fmt.Sprintf("  %s (mag %.2f)\n", s.Name, s.Mag)
	}
	return result
}

func info(query string) string {
	c := getConstellation(query)
	if c == nil {
		return fmt.Sprintf("❌ Constellation '%s' not found.", query)
	}
	return fmt.Sprintf("\n🌟 Constellation: %s\nAbbreviation: %s\nHemisphere: %s\nVisible Season: %s\nBrightest Star: %s\nMythology: %s\nNotable Objects: %s\n",
		c.Name, c.Abbr, c.Hemisphere, strings.Title(c.Season), c.BrightestStar, c.Mythology, c.NotableObjs)
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Usage: star_map <command> [options]")
		return
	}
	cmd := os.Args[1]

	switch cmd {
	case "list":
		listCmd := flag.NewFlagSet("list", flag.ExitOnError)
		hemisphere := listCmd.String("hemisphere", "all", "north, south, or all")
		listCmd.Parse(os.Args[2:])
		results := listAll(*hemisphere)
		fmt.Printf("\n📋 Constellations (%d):\n", len(results))
		for _, c := range results {
			fmt.Printf("  %s (%s) – %s\n", c.Name, c.Abbr, c.Hemisphere)
		}

	case "map":
		if len(os.Args) < 3 {
			fmt.Println("Error: need a constellation name or abbreviation")
			return
		}
		fmt.Print(drawMap(os.Args[2]))

	case "info":
		if len(os.Args) < 3 {
			fmt.Println("Error: need a constellation name or abbreviation")
			return
		}
		fmt.Print(info(os.Args[2]))

	case "season":
		if len(os.Args) < 3 {
			fmt.Println("Error: need a season (spring, summer, autumn, winter)")
			return
		}
		season := os.Args[2]
		results := bySeason(season)
		fmt.Printf("\n🌿 Constellations visible in %s:\n", strings.Title(season))
		for _, c := range results {
			fmt.Printf("  %s (%s)\n", c.Name, c.Abbr)
		}

	case "search":
		if len(os.Args) < 3 {
			fmt.Println("Error: need a search term")
			return
		}
		term := os.Args[2]
		results := search(term)
		if len(results) > 0 {
			fmt.Printf("\n🔍 Found %d constellation(s):\n", len(results))
			for _, c := range results {
				fmt.Printf("  %s (%s) – %s...\n", c.Name, c.Abbr, c.Mythology[:min(60, len(c.Mythology))])
			}
		} else {
			fmt.Println("❌ No matches found.")
		}

	default:
		fmt.Println("Unknown command. Use list, map, info, season, search.")
	}
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
