// star_map.js
#!/usr/bin/env node
const { program } = require('commander');

const CONSTELLATIONS = [
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
];

function getConstellation(query) {
    const lower = query.toLowerCase();
    return CONSTELLATIONS.find(c => c.name.toLowerCase() === lower || c.abbr.toLowerCase() === lower);
}

function listAll(hemisphere) {
    return CONSTELLATIONS.filter(c => hemisphere === 'all' || c.hemisphere === hemisphere || c.hemisphere === 'both');
}

function search(term) {
    const lower = term.toLowerCase();
    return CONSTELLATIONS.filter(c =>
        c.name.toLowerCase().includes(lower) ||
        c.abbr.toLowerCase().includes(lower) ||
        c.mythology.toLowerCase().includes(lower)
    );
}

function bySeason(season) {
    return CONSTELLATIONS.filter(c => c.season === season);
}

function drawLine(grid, x1, y1, x2, y2, char) {
    const dx = Math.abs(x2 - x1);
    const dy = Math.abs(y2 - y1);
    const sx = x1 < x2 ? 1 : -1;
    const sy = y1 < y2 ? 1 : -1;
    let err = dx - dy;
    let x = x1, y = y1;
    while (true) {
        if (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
            if (grid[y][x] !== '•') grid[y][x] = char;
        }
        if (x === x2 && y === y2) break;
        const e2 = 2 * err;
        if (e2 > -dy) { err -= dy; x += sx; }
        if (e2 < dx) { err += dx; y += sy; }
    }
}

function drawMap(query) {
    const c = getConstellation(query);
    if (!c) return `❌ Constellation '${query}' not found.`;
    let maxX = 0, maxY = 0;
    for (const s of c.stars) {
        if (s.x > maxX) maxX = s.x;
        if (s.y > maxY) maxY = s.y;
    }
    maxX += 2; maxY += 2;
    const grid = Array.from({length: maxY}, () => Array(maxX).fill(' '));
    for (const s of c.stars) grid[s.y][s.x] = '•';
    for (const [a,b] of c.connections) {
        const s1 = c.stars[a], s2 = c.stars[b];
        drawLine(grid, s1.x, s1.y, s2.x, s2.y, '·');
    }
    let result = `\n🌟 Constellation: ${c.name} (${c.abbr})\n\n`;
    for (const row of grid) result += '  ' + row.join('') + '\n';
    result += '\n';
    for (const s of c.stars) result += `  ${s.name} (mag ${s.mag.toFixed(2)})\n`;
    return result;
}

function info(query) {
    const c = getConstellation(query);
    if (!c) return `❌ Constellation '${query}' not found.`;
    return `\n🌟 Constellation: ${c.name}\nAbbreviation: ${c.abbr}\nHemisphere: ${c.hemisphere}\nVisible Season: ${c.season.charAt(0).toUpperCase() + c.season.slice(1)}\nBrightest Star: ${c.brightest_star}\nMythology: ${c.mythology}\nNotable Objects: ${c.notable_objects}`;
}

program
    .command('list')
    .option('--hemisphere <hemisphere>', 'north, south, or all', 'all')
    .action((options) => {
        const results = listAll(options.hemisphere);
        console.log(`\n📋 Constellations (${results.length}):`);
        for (const c of results) console.log(`  ${c.name} (${c.abbr}) – ${c.hemisphere}`);
    });

program
    .command('map <query>')
    .action((query) => console.log(drawMap(query)));

program
    .command('info <query>')
    .action((query) => console.log(info(query)));

program
    .command('season <season>')
    .action((season) => {
        const results = bySeason(season);
        console.log(`\n🌿 Constellations visible in ${season.charAt(0).toUpperCase() + season.slice(1)}:`);
        for (const c of results) console.log(`  ${c.name} (${c.abbr})`);
    });

program
    .command('search <term>')
    .action((term) => {
        const results = search(term);
        if (results.length > 0) {
            console.log(`\n🔍 Found ${results.length} constellation(s):`);
            for (const c of results) console.log(`  ${c.name} (${c.abbr}) – ${c.mythology.slice(0,60)}...`);
        } else {
            console.log('❌ No matches found.');
        }
    });

program.parse(process.argv);
