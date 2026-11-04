"""Exact chapter-II structure drawings from BRONZE_AGE_CONTENT_SPEC sections 7.1–7.12.

Coordinates are authored here, not scraped from Markdown on every build. Every
drawing is relative to its controller and explicitly reserves required air.
"""
from pathlib import Path
import json

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / 'src/main/resources/smog.bronze.json'
FRAME = 'smog:bronze_machine_frame'
AIR = 'minecraft:air'
B = 'smog:bronze_bearing_block'


def grid(controller, layers, symbols, label=None):
    cells = {}
    origin = None
    symbols = {'F': FRAME, 'B': B, ' ': AIR, **symbols}
    width = len(layers[0][0])
    for y, rows in enumerate(layers):
        for z, row in enumerate(rows):
            assert len(row) == width, (controller, row)
            for x, c in enumerate(row):
                if c == 'C':
                    assert origin is None
                    origin = (x, y, z)
                else:
                    cells[(x, y, z)] = symbols[c]
    assert origin is not None
    return drawing(controller, label or controller, origin, cells)


def drawing(controller, label, origin, cells):
    entries = []
    for (x, y, z), value in sorted(cells.items(), key=lambda kv: (kv[0][1], kv[0][2], kv[0][0])):
        if (x, y, z) == origin:
            continue
        entry = {'x': x-origin[0], 'y': y-origin[1], 'z': z-origin[2]}
        if value == 'floor':
            entry.update(blocks=[], floor=True)
        else:
            entry['blocks'] = value if isinstance(value, list) else [value]
        entries.append(entry)
    return {'id': label, 'controller': controller, 'cells': entries}


def generate():
    all = []
    hammer_layers = [['FCF', 'FMF', 'FFF'], ['F F', ' H ', 'F F'], ['FBF', 'FDF', 'FFF']]
    base = {'M': 'smog:bronze_stone_mortar', 'H': 'smog:bronze_hammer_arm', 'D': 'smog:bronze_cam_block'}
    all.append(grid('bronze_stamper', hammer_layers, base))
    all.append(grid('bronze_forge_hammer', hammer_layers, {**base, 'M': ['smog:forge_stone_anvil', 'minecraft:anvil', 'minecraft:chipped_anvil', 'minecraft:damaged_anvil']}))
    all.append(grid('bronze_mill', [['FCF', 'FLF', 'FFF'], ['FBF', 'FRF', 'FIF']], {'L': 'smog:bronze_grinding_bed', 'R': 'smog:bronze_grinding_runner', 'I': 'smog:bronze_machine_item_port'}))
    clamp = ['smog:wooden_tool_clamp', 'smog:bronze_tool_clamp']
    all.append(grid('bronze_spindle_bench', [['FCF', 'FTF'], ['FBF', 'FSK']], {'T': 'smog:bronze_work_table', 'S': 'smog:bronze_spindle_block', 'K': clamp}))
    all.append(grid('bronze_copy_bench', [['FCFF', 'FTTF'], ['FBHF', 'BSSK']], {'T': 'smog:bronze_work_table', 'H': 'smog:bronze_template_holder', 'S': 'smog:bronze_spindle_block', 'K': clamp}))
    for cylinders in (1, 2):
        all.append(grid('bronze_pump_station', [['FCF', 'FZF'], ['FBF', 'IVO'], ['FFF', 'FFF']], {'Z': 'smog:bronze_pump_cylinder', 'I': 'smog:bronze_pump_inlet', 'O': 'smog:bronze_pump_outlet', 'V': 'smog:water_valve' if cylinders == 1 else 'smog:bronze_pump_cylinder'}, 'bronze_pump_station_' + str(cylinders)))
    for sockets in (4, 8):
        c = {(x, y, z): (FRAME if y == 0 else AIR) for y in range(3) for x in range(5) for z in range(5)}
        c[(2, 0, 0)] = B
        c[(2, 1, 2)] = 'smog:bronze_casting_rotor'
        positions = [(2, 1), (3, 2), (2, 3), (1, 2)]
        if sockets == 8:
            positions += [(1, 1), (3, 1), (3, 3), (1, 3)]
        for x, z in positions:
            c[(x, 1, z)] = 'smog:bronze_casting_socket'
        c[(2, 2, 0)] = 'smog:casting_spout'
        c[(2, 2, 1)] = 'smog:casting_faucet'
        all.append(drawing('bronze_casting_carousel', 'bronze_casting_carousel_' + str(sockets), (2, 1, 0), c))
    c = {(x, y, z): 'smog:firebrick_casing' for x in range(3) for y in range(4) for z in range(3)}
    c.update({(1, 0, 1): 'smog:solid_firebox', (1, 1, 1): AIR, (1, 2, 1): AIR, (1, 1, 2): 'smog:air_nozzle', (1, 2, 0): 'smog:bloomery_work_door', (1, 3, 1): 'smog:kiln_vent'})
    all.append(drawing('bloomery_controller', 'bloomery_controller', (1, 1, 0), c))
    forge = grid('forge_hearth', [['FCF', 'FTF'], ['LWL', 'LNL']], {'F': 'smog:firebrick_casing', 'L': 'smog:firebrick_casing', 'T': 'smog:solid_firebox', 'W': 'smog:forge_work_bed', 'N': 'smog:air_nozzle'})
    forge['cells'].append({'x': 0, 'y': 2, 'z': 0, 'blocks': [AIR]})
    all.append(forge)
    for sections in range(1, 13):
        c = {(0, 0, 0): 'smog:bronze_bucket_bottom', (0, sections+1, 0): 'smog:bronze_bucket_top'}
        for y in range(1, sections+1):c[(0, y, 0)] = 'smog:bronze_bucket_section'
        all.append(drawing('bronze_bucket_elevator', 'bronze_bucket_elevator_' + str(sections), (0, 0, -1), c))
    for h in range(3, 25):
        c = {}
        for y in range(h+2):
            for x in (1, 2):
                for z in (1, 2):c[(x, y, z)] = 'smog:bronze_hoist_platform' if y == 0 else AIR
        for y in range(h+1):
            c[(0, y, 1)] = c[(3, y, 1)] = 'smog:bronze_hoist_guide'
            c[(3, y, 2)] = 'smog:bronze_hoist_counterweight' if y == h else AIR
        for x in (0, 3):
            for z in (0, 3):
                for y in range(h+2):c[(x, y, z)] = FRAME if y in (0, h+1) else [AIR, FRAME]
        for x in range(4):c[(x, h+1, 0)] = FRAME
        c[(1, h+1, 0)] = 'smog:bronze_hoist_drum'
        c[(2, h+1, 0)] = B
        c[(0, 0, 2)] = c[(0, h, 2)] = 'smog:bronze_hoist_landing'
        # Reserve the headroom above the complete moving platform.
        for x in (1, 2):
            for z in (1, 2):c[(x, h+2, z)] = AIR
        all.append(drawing('bronze_mine_hoist', 'bronze_mine_hoist_' + str(h), (1, h+1, 3), c))
    c = {(x, -1, z): 'floor' for x in range(-3, 4) for z in range(-3, 4)}
    c.update({(x, y, z): AIR for x in range(-3, 4) for z in range(-3, 4) for y in (0, 1)})
    c[(0, 0, 0)] = 'smog:bronze_capstan_post'
    c[(1, 1, 0)] = 'smog:bronze_capstan_arm'
    all.append(drawing('bronze_animal_capstan', 'bronze_animal_capstan', (0, 0, -1), c))
    all.append(drawing('bronze_finishing_attachment', 'bronze_finishing_attachment', (0, 0, 0), {(1, 0, 0): 'smog:tool_workbench'}))
    catalog = json.loads(CATALOG.read_text(encoding='utf-8'));catalog['blueprints'] = all
    CATALOG.write_text(json.dumps(catalog, ensure_ascii=False, indent=2)+'\n', encoding='utf-8')
    print(f'{len(all)} drawings, {len(set(b["controller"] for b in all))} controllers; variable heights and explicit alternatives')


if __name__ == '__main__':generate()
