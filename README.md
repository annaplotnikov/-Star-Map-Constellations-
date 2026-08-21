🌟 Star Map (Constellations) — Multi‑Language Celestial Atlas
8 languages, one interactive star map – explore constellations, view ASCII star charts, discover mythology, and navigate the night sky from your terminal.

✨ Features
🗺️ ASCII star map – visualize constellations with connected stars

📋 List all constellations – names, abbreviations, and hemispheres

📖 Detailed descriptions – mythology, brightest stars, and notable objects

🌍 Seasonal visibility – see which constellations are visible in each season

🔍 Search – by name, abbreviation, or mythology keyword

⭐ Brightest stars – learn the name and magnitude of each constellation's brightest star

📊 Interactive map – zoom in/out on the celestial sphere (simple CLI view)

💾 No external API – all data is built‑in

🚀 Quick Start
All implementations follow the same CLI pattern:

bash
# List all constellations
<command> list

# List only Northern constellations
<command> list --hemisphere north

# Show a star map of a constellation
<command> map Orion

# Get detailed info about a constellation
<command> info Orion

# Search by abbreviation
<command> info Ori

# Show visible constellations in summer
<command> season summer

# Search through descriptions and names
<command> search "warrior"
Commands/Arguments:

list [--hemisphere north|south|all] – show all constellations

map <name|abbr> – display an ASCII star map

info <name|abbr> – detailed info about a specific constellation

season <spring|summer|autumn|winter> – constellations visible in a season

search <term> – search names and descriptions

📸 Example Output (Star Map)
text
🌟 Constellation: Orion (Ori)

    * Betelgeuse
     \     *
      \   /
       \ /
        *  *  *  (Belt)
       / \
      /   \
     /     *
    *
 Rigel
📁 Repository Structure
text
.
├── README.md
├── python/
│   └── star_map.py
├── go/
│   └── star_map.go
├── javascript/
│   └── star_map.js
├── ruby/
│   └── star_map.rb
├── php/
│   └── star_map.php
├── java/
│   └── StarMap.java
├── csharp/
│   └── StarMap.cs
└── cpp/
