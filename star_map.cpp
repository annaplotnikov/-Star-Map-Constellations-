// star_map.cpp
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <cmath>
#include <cctype>
#include <algorithm>
#include <nlohmann/json.hpp>
#include <getopt.h>

using namespace std;
using json = nlohmann::json;

struct Star {
    int x, y;
    string name;
    double mag;
};

struct Constellation {
    string name, abbr, hemisphere, season, brightest_star, mythology, notable_objects;
    vector<Star> stars;
    vector<pair<int,int>> connections;
};

vector<Constellation> CONSTELLATIONS = {
    {
        "Orion", "Ori", "both", "winter",
        "Betelgeuse (Alpha Orionis, 0.42 mag)",
        "Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.",
        "Orion Nebula (M42), Horsehead Nebula, Barnard's Loop",
        {{4,1,"Betelgeuse",0.42},{5,4,"Bellatrix",1.64},{3,5,"Mintaka",2.23},{5,5,"Alnilam",1.69},{7,5,"Alnitak",1.88},{2,8,"Saiph",2.06},{6,9,"Rigel",0.12}},
        {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{1,2},{2,3},{3,4},{4,5},{5,6},{2,4}}
    },
    {
        "Ursa Major", "UMa", "north", "spring",
        "Alioth (Epsilon Ursae Majoris, 1.77 mag)",
        "Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.",
        "M81, M82 (Bode's Galaxy and Cigar Galaxy), Owl Nebula (M97)",
        {{1,1,"Dubhe",1.79},{3,2,"Merak",2.34},{2,4,"Phecda",2.41},{4,4,"Megrez",3.31},{5,5,"Alioth",1.77},{7,4,"Mizar",2.04},{9,3,"Alkaid",1.86}},
        {{0,1},{1,2},{2,3},{3,4},{4,5},{5,6}}
    },
    {
        "Cassiopeia", "Cas", "north", "autumn",
        "Schedar (Alpha Cassiopeiae, 2.24 mag)",
        "Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.",
        "Cassiopeia A (supernova remnant), NGC 457 (open cluster)",
        {{2,1,"Segin",3.37},{3,3,"Ruchbah",2.68},{5,2,"Schedar",2.24},{6,4,"Navi",2.47},{8,3,"Caph",2.28}},
        {{0,1},{1,2},{2,3},{3,4},{0,2},{2,4}}
    },
    {
        "Scorpius", "Sco", "south", "summer",
        "Antares (Alpha Scorpii, 0.96 mag)",
        "Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.",
        "Antares (red supergiant), Ptolemy's Cluster (M7), Butterfly Cluster (M6)",
        {{2,1,"Antares",0.96},{3,3,"Graffias",2.56},{5,4,"Dschubba",2.29},{6,5,"Wei",2.62},{8,6,"Shaula",1.62}},
        {{0,1},{1,2},{2,3},{3,4}}
    },
    {
        "Lyra", "Lyr", "north", "summer",
        "Vega (Alpha Lyrae, 0.03 mag)",
        "Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.",
        "Ring Nebula (M57), Vega (brightest star)",
        {{5,1,"Vega",0.03},{3,4,"Sheliak",3.25},{7,4,"Sulafat",3.24}},
        {{0,1},{0,2},{1,2}}
    }
};

string toLower(const string& s) {
    string r = s;
    for (char& c : r) c = tolower(c);
    return r;
}

const Constellation* getConstellation(const string& query) {
    string q = toLower(query);
    for (const auto& c : CONSTELLATIONS) {
        if (toLower(c.name) == q || toLower(c.abbr) == q) return &c;
    }
    return nullptr;
}

vector<const Constellation*> listAll(const string& hemisphere) {
    vector<const Constellation*> result;
    for (const auto& c : CONSTELLATIONS) {
        if (hemisphere == "all" || c.hemisphere == hemisphere || c.hemisphere == "both") {
            result.push_back(&c);
        }
    }
    return result;
}

vector<const Constellation*> search(const string& term) {
    string t = toLower(term);
    vector<const Constellation*> result;
    for (const auto& c : CONSTELLATIONS) {
        if (toLower(c.name).find(t) != string::npos ||
            toLower(c.abbr).find(t) != string::npos ||
            toLower(c.mythology).find(t) != string::npos) {
            result.push_back(&c);
        }
    }
    return result;
}

vector<const Constellation*> bySeason(const string& season) {
    vector<const Constellation*> result;
    for (const auto& c : CONSTELLATIONS) {
        if (c.season == season) result.push_back(&c);
    }
    return result;
}

void drawLine(vector<vector<char>>& grid, int x1, int y1, int x2, int y2, char ch) {
    int dx = abs(x2 - x1);
    int dy = abs(y2 - y1);
    int sx = x1 < x2 ? 1 : -1;
    int sy = y1 < y2 ? 1 : -1;
    int err = dx - dy;
    int x = x1, y = y1;
    while (true) {
        if (y >= 0 && y < (int)grid.size() && x >= 0 && x < (int)grid[0].size()) {
            if (grid[y][x] != '•') grid[y][x] = ch;
        }
        if (x == x2 && y == y2) break;
        int e2 = 2 * err;
        if (e2 > -dy) { err -= dy; x += sx; }
        if (e2 < dx) { err += dx; y += sy; }
    }
}

string drawMap(const string& query) {
    const Constellation* c = getConstellation(query);
    if (!c) return "❌ Constellation '" + query + "' not found.";
    int maxX = 0, maxY = 0;
    for (const auto& s : c->stars) {
        if (s.x > maxX) maxX = s.x;
        if (s.y > maxY) maxY = s.y;
    }
    maxX += 2; maxY += 2;
    vector<vector<char>> grid(maxY, vector<char>(maxX, ' '));
    for (const auto& s : c->stars) grid[s.y][s.x] = '•';
    for (const auto& conn : c->connections) {
        const Star& s1 = c->stars[conn.first];
        const Star& s2 = c->stars[conn.second];
        drawLine(grid, s1.x, s1.y, s2.x, s2.y, '·');
    }
    string result = "\n🌟 Constellation: " + c->name + " (" + c->abbr + ")\n\n";
    for (const auto& row : grid) {
        result += "  ";
        for (char ch : row) result += ch;
        result += "\n";
    }
    result += "\n";
    for (const auto& s : c->stars) {
        result += "  " + s.name + " (mag " + to_string(s.mag).substr(0,4) + ")\n";
    }
    return result;
}

string info(const string& query) {
    const Constellation* c = getConstellation(query);
    if (!c) return "❌ Constellation '" + query + "' not found.";
    string season = c->season;
    season[0] = toupper(season[0]);
    return "\n🌟 Constellation: " + c->name + "\nAbbreviation: " + c->abbr +
           "\nHemisphere: " + c->hemisphere + "\nVisible Season: " + season +
           "\nBrightest Star: " + c->brightest_star + "\nMythology: " + c->mythology +
           "\nNotable Objects: " + c->notable_objects;
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        cerr << "Usage: star_map <command> [options]\n";
        return 1;
    }
    string cmd = argv[1];

    if (cmd == "list") {
        string hemisphere = "all";
        for (int i=2; i<argc; i++) {
            if (string(argv[i]) == "--hemisphere" && i+1 < argc) {
                hemisphere = argv[++i];
            }
        }
        auto results = listAll(hemisphere);
        cout << "\n📋 Constellations (" << results.size() << "):\n";
        for (const auto* c : results) {
            cout << "  " << c->name << " (" << c->abbr << ") – " << c->hemisphere << "\n";
        }
    } else if (cmd == "map") {
        if (argc < 3) { cerr << "Error: need a constellation name or abbreviation\n"; return 1; }
        cout << drawMap(argv[2]);
    } else if (cmd == "info") {
        if (argc < 3) { cerr << "Error: need a constellation name or abbreviation\n"; return 1; }
        cout << info(argv[2]);
    } else if (cmd == "season") {
        if (argc < 3) { cerr << "Error: need a season (spring, summer, autumn, winter)\n"; return 1; }
        auto results = bySeason(argv[2]);
        string s = argv[2];
        s[0] = toupper(s[0]);
        cout << "\n🌿 Constellations visible in " << s << ":\n";
        for (const auto* c : results) {
            cout << "  " << c->name << " (" << c->abbr << ")\n";
        }
    } else if (cmd == "search") {
        if (argc < 3) { cerr << "Error: need a search term\n"; return 1; }
        auto results = search(argv[2]);
        if (!results.empty()) {
            cout << "\n🔍 Found " << results.size() << " constellation(s):\n";
            for (const auto* c : results) {
                string myth = c->mythology;
                if (myth.length() > 60) myth = myth.substr(0,60) + "...";
                cout << "  " << c->name << " (" << c->abbr << ") – " << myth << "\n";
            }
        } else {
            cout << "❌ No matches found.\n";
        }
    } else {
        cerr << "Unknown command. Use list, map, info, season, search.\n";
        return 1;
    }
    return 0;
}
