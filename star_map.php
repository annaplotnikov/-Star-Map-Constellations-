# star_map.php
#!/usr/bin/env php
<?php

$constellations = [
    [
        'name' => 'Orion', 'abbr' => 'Ori', 'hemisphere' => 'both', 'season' => 'winter',
        'brightest_star' => 'Betelgeuse (Alpha Orionis, 0.42 mag)',
        'mythology' => 'Orion, a mighty hunter in Greek mythology, was placed among the stars by Zeus. He is depicted as a hunter with his belt, sword, and club.',
        'notable_objects' => 'Orion Nebula (M42), Horsehead Nebula, Barnard\'s Loop',
        'stars' => [['x'=>4,'y'=>1,'name'=>'Betelgeuse','mag'=>0.42],['x'=>5,'y'=>4,'name'=>'Bellatrix','mag'=>1.64],['x'=>3,'y'=>5,'name'=>'Mintaka','mag'=>2.23],['x'=>5,'y'=>5,'name'=>'Alnilam','mag'=>1.69],['x'=>7,'y'=>5,'name'=>'Alnitak','mag'=>1.88],['x'=>2,'y'=>8,'name'=>'Saiph','mag'=>2.06],['x'=>6,'y'=>9,'name'=>'Rigel','mag'=>0.12]],
        'connections' => [[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],[1,2],[2,3],[3,4],[4,5],[5,6],[2,4]]
    ],
    [
        'name' => 'Ursa Major', 'abbr' => 'UMa', 'hemisphere' => 'north', 'season' => 'spring',
        'brightest_star' => 'Alioth (Epsilon Ursae Majoris, 1.77 mag)',
        'mythology' => 'Ursa Major represents the Great Bear. In Greek mythology, Callisto was transformed into a bear by Hera and placed in the sky by Zeus.',
        'notable_objects' => 'M81, M82 (Bode\'s Galaxy and Cigar Galaxy), Owl Nebula (M97)',
        'stars' => [['x'=>1,'y'=>1,'name'=>'Dubhe','mag'=>1.79],['x'=>3,'y'=>2,'name'=>'Merak','mag'=>2.34],['x'=>2,'y'=>4,'name'=>'Phecda','mag'=>2.41],['x'=>4,'y'=>4,'name'=>'Megrez','mag'=>3.31],['x'=>5,'y'=>5,'name'=>'Alioth','mag'=>1.77],['x'=>7,'y'=>4,'name'=>'Mizar','mag'=>2.04],['x'=>9,'y'=>3,'name'=>'Alkaid','mag'=>1.86]],
        'connections' => [[0,1],[1,2],[2,3],[3,4],[4,5],[5,6]]
    ],
    [
        'name' => 'Cassiopeia', 'abbr' => 'Cas', 'hemisphere' => 'north', 'season' => 'autumn',
        'brightest_star' => 'Schedar (Alpha Cassiopeiae, 2.24 mag)',
        'mythology' => 'Cassiopeia was the vain queen of Ethiopia who boasted about her beauty, leading to the sacrifice of her daughter Andromeda. She was placed in the sky as punishment.',
        'notable_objects' => 'Cassiopeia A (supernova remnant), NGC 457 (open cluster)',
        'stars' => [['x'=>2,'y'=>1,'name'=>'Segin','mag'=>3.37],['x'=>3,'y'=>3,'name'=>'Ruchbah','mag'=>2.68],['x'=>5,'y'=>2,'name'=>'Schedar','mag'=>2.24],['x'=>6,'y'=>4,'name'=>'Navi','mag'=>2.47],['x'=>8,'y'=>3,'name'=>'Caph','mag'=>2.28]],
        'connections' => [[0,1],[1,2],[2,3],[3,4],[0,2],[2,4]]
    ],
    [
        'name' => 'Scorpius', 'abbr' => 'Sco', 'hemisphere' => 'south', 'season' => 'summer',
        'brightest_star' => 'Antares (Alpha Scorpii, 0.96 mag)',
        'mythology' => 'Scorpius represents the scorpion that killed Orion. According to mythology, the scorpion was placed on the opposite side of the sky from Orion.',
        'notable_objects' => 'Antares (red supergiant), Ptolemy\'s Cluster (M7), Butterfly Cluster (M6)',
        'stars' => [['x'=>2,'y'=>1,'name'=>'Antares','mag'=>0.96],['x'=>3,'y'=>3,'name'=>'Graffias','mag'=>2.56],['x'=>5,'y'=>4,'name'=>'Dschubba','mag'=>2.29],['x'=>6,'y'=>5,'name'=>'Wei','mag'=>2.62],['x'=>8,'y'=>6,'name'=>'Shaula','mag'=>1.62]],
        'connections' => [[0,1],[1,2],[2,3],[3,4]]
    ],
    [
        'name' => 'Lyra', 'abbr' => 'Lyr', 'hemisphere' => 'north', 'season' => 'summer',
        'brightest_star' => 'Vega (Alpha Lyrae, 0.03 mag)',
        'mythology' => 'Lyra represents the lyre of Orpheus, the legendary musician and poet. After his death, the lyre was placed among the stars.',
        'notable_objects' => 'Ring Nebula (M57), Vega (brightest star)',
        'stars' => [['x'=>5,'y'=>1,'name'=>'Vega','mag'=>0.03],['x'=>3,'y'=>4,'name'=>'Sheliak','mag'=>3.25],['x'=>7,'y'=>4,'name'=>'Sulafat','mag'=>3.24]],
        'connections' => [[0,1],[0,2],[1,2]]
    ]
];

function getConstellation($query) {
    global $constellations;
    $lower = strtolower($query);
    foreach ($constellations as $c) {
        if (strtolower($c['name']) == $lower || strtolower($c['abbr']) == $lower) return $c;
    }
    return null;
}

function listAll($hemisphere) {
    global $constellations;
    return array_filter($constellations, function($c) use ($hemisphere) {
        return $hemisphere == 'all' || $c['hemisphere'] == $hemisphere || $c['hemisphere'] == 'both';
    });
}

function search($term) {
    global $constellations;
    $lower = strtolower($term);
    return array_filter($constellations, function($c) use ($lower) {
        return strpos(strtolower($c['name']), $lower) !== false ||
               strpos(strtolower($c['abbr']), $lower) !== false ||
               strpos(strtolower($c['mythology']), $lower) !== false;
    });
}

function bySeason($season) {
    global $constellations;
    return array_filter($constellations, function($c) use ($season) {
        return $c['season'] == $season;
    });
}

function drawLine(&$grid, $x1, $y1, $x2, $y2, $char) {
    $dx = abs($x2 - $x1);
    $dy = abs($y2 - $y1);
    $sx = $x1 < $x2 ? 1 : -1;
    $sy = $y1 < $y2 ? 1 : -1;
    $err = $dx - $dy;
    $x = $x1; $y = $y1;
    while (true) {
        if ($y >= 0 && $y < count($grid) && $x >= 0 && $x < count($grid[0])) {
            if ($grid[$y][$x] != '•') $grid[$y][$x] = $char;
        }
        if ($x == $x2 && $y == $y2) break;
        $e2 = 2 * $err;
        if ($e2 > -$dy) { $err -= $dy; $x += $sx; }
        if ($e2 < $dx) { $err += $dx; $y += $sy; }
    }
}

function drawMap($query) {
    $c = getConstellation($query);
    if (!$c) return "❌ Constellation '$query' not found.";
    $maxX = 0; $maxY = 0;
    foreach ($c['stars'] as $s) {
        if ($s['x'] > $maxX) $maxX = $s['x'];
        if ($s['y'] > $maxY) $maxY = $s['y'];
    }
    $maxX += 2; $maxY += 2;
    $grid = array_fill(0, $maxY, array_fill(0, $maxX, ' '));
    foreach ($c['stars'] as $s) $grid[$s['y']][$s['x']] = '•';
    foreach ($c['connections'] as $conn) {
        $s1 = $c['stars'][$conn[0]];
        $s2 = $c['stars'][$conn[1]];
        drawLine($grid, $s1['x'], $s1['y'], $s2['x'], $s2['y'], '·');
    }
    $result = "\n🌟 Constellation: {$c['name']} ({$c['abbr']})\n\n";
    foreach ($grid as $row) $result .= '  ' . implode('', $row) . "\n";
    $result .= "\n";
    foreach ($c['stars'] as $s) $result .= "  {$s['name']} (mag " . number_format($s['mag'], 2) . ")\n";
    return $result;
}

function info($query) {
    $c = getConstellation($query);
    if (!$c) return "❌ Constellation '$query' not found.";
    return "\n🌟 Constellation: {$c['name']}\nAbbreviation: {$c['abbr']}\nHemisphere: {$c['hemisphere']}\nVisible Season: " . ucfirst($c['season']) . "\nBrightest Star: {$c['brightest_star']}\nMythology: {$c['mythology']}\nNotable Objects: {$c['notable_objects']}";
}

if ($argc < 2) {
    die("Usage: php star_map.php <command> [options]\n");
}
$cmd = $argv[1];

switch ($cmd) {
    case 'list':
        $hemisphere = 'all';
        for ($i=2; $i<$argc; $i++) {
            if ($argv[$i] == '--hemisphere' && isset($argv[$i+1])) {
                $hemisphere = $argv[$i+1];
                $i++;
            }
        }
        $results = listAll($hemisphere);
        echo "\n📋 Constellations (" . count($results) . "):\n";
        foreach ($results as $c) {
            echo "  {$c['name']} ({$c['abbr']}) – {$c['hemisphere']}\n";
        }
        break;
    case 'map':
        if ($argc < 3) die("Error: need a constellation name or abbreviation\n");
        echo drawMap($argv[2]);
        break;
    case 'info':
        if ($argc < 3) die("Error: need a constellation name or abbreviation\n");
        echo info($argv[2]);
        break;
    case 'season':
        if ($argc < 3) die("Error: need a season (spring, summer, autumn, winter)\n");
        $results = bySeason($argv[2]);
        echo "\n🌿 Constellations visible in " . ucfirst($argv[2]) . ":\n";
        foreach ($results as $c) {
            echo "  {$c['name']} ({$c['abbr']})\n";
        }
        break;
    case 'search':
        if ($argc < 3) die("Error: need a search term\n");
        $results = search($argv[2]);
        if (count($results) > 0) {
            echo "\n🔍 Found " . count($results) . " constellation(s):\n";
            foreach ($results as $c) {
                echo "  {$c['name']} ({$c['abbr']}) – " . substr($c['mythology'], 0, 60) . "...\n";
            }
        } else {
            echo "❌ No matches found.\n";
        }
        break;
    default:
        echo "Unknown command. Use list, map, info, season, search.\n";
}
?>
