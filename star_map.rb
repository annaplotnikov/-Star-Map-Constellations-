# star_map.rb
#!/usr/bin/env ruby
require 'json'
require 'optparse'

CONSTELLATIONS = [
  {
    name: "Orion", abbr: "Ori", hemisphere: "both", season: "winter",
    brightest_star: "Betelgeuse (Alpha Orionis, 0.42 mag)",
    mythology: "Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.",
    notable_objects: "Orion Nebula (M42), Horsehead Nebula, Barnard's Loop",
    stars: [{x:4,y:1,name:"Betelgeuse",mag:0.42},{x:5,y:4,name:"Bellatrix",mag:1.64},{x:3,y:5,name:"Mintaka",mag:2.23},{x:5,y:5,name:"Alnilam",mag:1.69},{x:7,y:5,name:"Alnitak",mag:1.88},{x:2,y:8,name:"Saiph",mag:2.06},{x:6,y:9,name:"Rigel",mag:0.12}],
    connections: [[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],[1,2],[2,3],[3,4],[4,5],[5,6],[2,4]]
  },
  {
    name: "Ursa Major", abbr: "UMa", hemisphere: "north", season: "spring",
    brightest_star: "Alioth (Epsilon Ursae Majoris, 1.77 mag)",
    mythology: "Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.",
    notable_objects: "M81, M82 (Bode's Galaxy and Cigar Galaxy), Owl Nebula (M97)",
    stars: [{x:1,y:1,name:"Dubhe",mag:1.79},{x:3,y:2,name:"Merak",mag:2.34},{x:2,y:4,name:"Phecda",mag:2.41},{x:4,y:4,name:"Megrez",mag:3.31},{x:5,y:5,name:"Alioth",mag:1.77},{x:7,y:4,name:"Mizar",mag:2.04},{x:9,y:3,name:"Alkaid",mag:1.86}],
    connections: [[0,1],[1,2],[2,3],[3,4],[4,5],[5,6]]
  },
  {
    name: "Cassiopeia", abbr: "Cas", hemisphere: "north", season: "autumn",
    brightest_star: "Schedar (Alpha Cassiopeiae, 2.24 mag)",
    mythology: "Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.",
    notable_objects: "Cassiopeia A (supernova remnant), NGC 457 (open cluster)",
    stars: [{x:2,y:1,name:"Segin",mag:3.37},{x:3,y:3,name:"Ruchbah",mag:2.68},{x:5,y:2,name:"Schedar",mag:2.24},{x:6,y:4,name:"Navi",mag:2.47},{x:8,y:3,name:"Caph",mag:2.28}],
    connections: [[0,1],[1,2],[2,3],[3,4],[0,2],[2,4]]
  },
  {
    name: "Scorpius", abbr: "Sco", hemisphere: "south", season: "summer",
    brightest_star: "Antares (Alpha Scorpii, 0.96 mag)",
    mythology: "Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.",
    notable_objects: "Antares (red supergiant), Ptolemy's Cluster (M7), Butterfly Cluster (M6)",
    stars: [{x:2,y:1,name:"Antares",mag:0.96},{x:3,y:3,name:"Graffias",mag:2.56},{x:5,y:4,name:"Dschubba",mag:2.29},{x:6,y:5,name:"Wei",mag:2.62},{x:8,y:6,name:"Shaula",mag:1.62}],
    connections: [[0,1],[1,2],[2,3],[3,4]]
  },
  {
    name: "Lyra", abbr: "Lyr", hemisphere: "north", season: "summer",
    brightest_star: "Vega (Alpha Lyrae, 0.03 mag)",
    mythology: "Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.",
    notable_objects: "Ring Nebula (M57), Vega (brightest star)",
    stars: [{x:5,y:1,name:"Vega",mag:0.03},{x:3,y:4,name:"Sheliak",mag:3.25},{x:7,y:4,name:"Sulafat",mag:3.24}],
    connections: [[0,1],[0,2],[1,2]]
  }
]

def get_constellation(query)
  CONSTELLATIONS.find { |c| c[:name].downcase == query.downcase || c[:abbr].downcase == query.downcase }
end

def list_all(hemisphere)
  CONSTELLATIONS.select { |c| hemisphere == 'all' || c[:hemisphere] == hemisphere || c[:hemisphere] == 'both' }
end

def search(term)
  CONSTELLATIONS.select { |c| c[:name].downcase.include?(term.downcase) || c[:abbr].downcase.include?(term.downcase) || c[:mythology].downcase.include?(term.downcase) }
end

def by_season(season)
  CONSTELLATIONS.select { |c| c[:season] == season }
end

def draw_line(grid, x1, y1, x2, y2, char)
  dx = (x2 - x1).abs
  dy = (y2 - y1).abs
  sx = x1 < x2 ? 1 : -1
  sy = y1 < y2 ? 1 : -1
  err = dx - dy
  x, y = x1, y1
  while true
    if y >= 0 && y < grid.size && x >= 0 && x < grid[0].size
      grid[y][x] = char if grid[y][x] != "•"
    end
    break if x == x2 && y == y2
    e2 = 2 * err
    if e2 > -dy
      err -= dy
      x += sx
    end
    if e2 < dx
      err += dx
      y += sy
    end
  end
end

def draw_map(query)
  c = get_constellation(query)
  return "❌ Constellation '#{query}' not found." unless c
  max_x = c[:stars].map { |s| s[:x] }.max + 2
  max_y = c[:stars].map { |s| s[:y] }.max + 2
  grid = Array.new(max_y) { Array.new(max_x, " ") }
  c[:stars].each { |s| grid[s[:y]][s[:x]] = "•" }
  c[:connections].each do |a, b|
    s1 = c[:stars][a]
    s2 = c[:stars][b]
    draw_line(grid, s1[:x], s1[:y], s2[:x], s2[:y], "·")
  end
  result = "\n🌟 Constellation: #{c[:name]} (#{c[:abbr]})\n\n"
  grid.each { |row| result += "  " + row.join + "\n" }
  result += "\n"
  c[:stars].each { |s| result += "  #{s[:name]} (mag #{s[:mag].round(2)})\n" }
  result
end

def info(query)
  c = get_constellation(query)
  return "❌ Constellation '#{query}' not found." unless c
  <<~INFO
    \n🌟 Constellation: #{c[:name]}
    Abbreviation: #{c[:abbr]}
    Hemisphere: #{c[:hemisphere]}
    Visible Season: #{c[:season].capitalize}
    Brightest Star: #{c[:brightest_star]}
    Mythology: #{c[:mythology]}
    Notable Objects: #{c[:notable_objects]}
  INFO
end

options = {}
$command = ARGV.shift
if $command.nil?
  puts "Usage: star_map.rb <command> [options]"
  exit 1
end

case $command
when "list"
  hemisphere = "all"
  if ARGV.include?("--hemisphere")
    idx = ARGV.index("--hemisphere")
    hemisphere = ARGV[idx+1] if idx
  end
  results = list_all(hemisphere)
  puts "\n📋 Constellations (#{results.length}):"
  results.each { |c| puts "  #{c[:name]} (#{c[:abbr]}) – #{c[:hemisphere]}" }

when "map"
  query = ARGV.shift
  if query.nil?
    puts "Error: need a constellation name or abbreviation"
    exit 1
  end
  puts draw_map(query)

when "info"
  query = ARGV.shift
  if query.nil?
    puts "Error: need a constellation name or abbreviation"
    exit 1
  end
  puts info(query)

when "season"
  season = ARGV.shift
  if season.nil?
    puts "Error: need a season (spring, summer, autumn, winter)"
    exit 1
  end
  results = by_season(season)
  puts "\n🌿 Constellations visible in #{season.capitalize}:"
  results.each { |c| puts "  #{c[:name]} (#{c[:abbr]})" }

when "search"
  term = ARGV.shift
  if term.nil?
    puts "Error: need a search term"
    exit 1
  end
  results = search(term)
  if results.length > 0
    puts "\n🔍 Found #{results.length} constellation(s):"
    results.each { |c| puts "  #{c[:name]} (#{c[:abbr]}) – #{c[:mythology][0..60]}..." }
  else
    puts "❌ No matches found."
  end

else
  puts "Unknown command. Use list, map, info, season, search."
end
