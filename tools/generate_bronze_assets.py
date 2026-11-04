"""Reproducible native voxel assets for the Bronze Drive registration stage.

Only bronze-prefixed assets listed in smog.bronze.json are written. Existing copper
art and artist-authored models are deliberately not used as output targets.
"""
from pathlib import Path
import json
import math
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/smog'
DATA = ROOT / 'src/main/resources/data/smog'
CATALOG = ROOT / 'src/main/resources/smog.bronze.json'
FACES = ('north', 'south', 'east', 'west', 'up', 'down')


def write(path, obj):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(obj, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')


def cuboid(a, b, texture='wood'):
    return {'from': list(a), 'to': list(b), 'faces': {side: {'texture': '#' + texture} for side in FACES}}


def exterior(elements):
    """Clip buried and coincident faces, preserving one native texture pixel per unit.

    Later cuboids win a coplanar material seam. Static and moving meshes are clipped
    independently, so moving a rotor never reveals a hole cut in its housing.
    """
    output = []
    for index, e in enumerate(elements):
        for side, face in e['faces'].items():
            axis, positive, u, v = {'west': (0, False, 2, 1), 'east': (0, True, 2, 1), 'north': (2, False, 0, 1), 'south': (2, True, 0, 1), 'down': (1, False, 0, 2), 'up': (1, True, 0, 2)}[side]
            plane = e['to' if positive else 'from'][axis]
            rects = [(e['from'][u], e['to'][u], e['from'][v], e['to'][v])]
            for j, other in enumerate(elements):
                if index == j:continue
                lo, hi = other['from'][axis], other['to'][axis]
                covered = (lo <= plane < hi or hi == plane and j > index) if positive else (lo < plane <= hi or lo == plane and j > index)
                if not covered:continue
                next_rects = []
                for a, b, c, d in rects:
                    x, xx = max(a, other['from'][u]), min(b, other['to'][u])
                    y, yy = max(c, other['from'][v]), min(d, other['to'][v])
                    if x >= xx or y >= yy:
                        next_rects.append((a, b, c, d));continue
                    for r in [(a, x, c, d), (xx, b, c, d), (x, xx, c, y), (x, xx, yy, d)]:
                        if r[0] < r[1] and r[2] < r[3]:next_rects.append(r)
                rects = next_rects
                if not rects:break
            for a, b, c, d in rects:
                low, high = list(e['from']), list(e['to']);low[u], high[u], low[v], high[v] = a, b, c, d
                shift_u, shift_v = math.floor(a / 16) * 16, math.floor(c / 16) * 16
                output.append({'from': low, 'to': high, 'faces': {side: {**face, 'uv': [a-shift_u, 16-d+shift_v, b-shift_u, 16-c+shift_v]}}})
    return output


def ring(axis='z', radius=6, inner=3.5, depth=3, texture='metal', center=(8, 8, 8)):
    """A stepped circular rim, with a genuinely empty centre and no alpha planes."""
    result = []
    for u in range(-int(radius), int(radius)):
        for v in range(-int(radius), int(radius)):
            r = math.hypot(u + .5, v + .5)
            if inner <= r <= radius:
                lo, hi = [0., 0., 0.], [0., 0., 0.]
                n = 'xyz'.index(axis)
                uv = [i for i in range(3) if i != n]
                lo[n], hi[n] = center[n] - depth / 2, center[n] + depth / 2
                lo[uv[0]], hi[uv[0]] = center[uv[0]] + u, center[uv[0]] + u + 1
                lo[uv[1]], hi[uv[1]] = center[uv[1]] + v, center[uv[1]] + v + 1
                result.append(cuboid(lo, hi, texture))
    return result


def frame():
    es = []
    for x in (0, 14):
        for z in (0, 14):
            es += [cuboid((x, 0, z), (x + 2, 16, z + 2)), cuboid((x, 1, z), (x + 2, 2, z + 2), 'metal')]
    for y in (0, 14):
        es += [cuboid((0, y, 0), (16, y + 2, 2)), cuboid((0, y, 14), (16, y + 2, 16))]
    return es


def geometry(entry):
    n = entry['id']
    fixed, moving = [], []
    box = lambda a, b, t='wood': cuboid(a, b, t)
    if n == 'bronze_machine_frame':
        fixed = frame()
    elif n in ('bronze_shaft', 'bronze_shaft_support', 'bronze_bearing_block'):
        moving = [box((6, 6, 0), (10, 10, 16)), box((5.5, 5.5, 0), (10.5, 10.5, 2), 'metal'), box((5.5, 5.5, 14), (10.5, 10.5, 16), 'metal')]
        if n != 'bronze_shaft':
            fixed = [box((1, 0, 3), (15, 3, 13), 'stone'), box((2, 3, 4), (5, 12, 12)), box((11, 3, 4), (14, 12, 12))] + ring(radius=5, inner=3, depth=5)
    elif n in ('bronze_angle_gearbox', 'bronze_reduction_gearbox', 'bronze_splitter', 'bronze_clutch', 'bronze_safety_clutch', 'bronze_drive_coupler'):
        fixed = [box((1, 0, 1), (15, 3, 15)), box((1, 3, 1), (4, 13, 15)), box((12, 3, 1), (15, 13, 15)), box((1, 13, 1), (15, 15, 15))]
        moving = ring(radius=5, inner=2.5, depth=3) + [box((6, 6, 0), (10, 10, 16), 'metal')]
        if n in ('bronze_angle_gearbox', 'bronze_splitter'):
            fixed += [box((0, 6, 6), (5, 10, 10), 'metal')]
        if n == 'bronze_splitter':
            fixed += [box((11, 6, 6), (16, 10, 10), 'metal')]
        if n == 'bronze_safety_clutch':
            fixed += [box((4, 14, 3), (12, 16, 5), 'dark')]
    elif n in ('bronze_pulley', 'bronze_flywheel', 'bronze_hand_crank'):
        fixed = [box((2, 0, 5), (14, 2, 11)), box((6, 2, 10), (10, 12, 13))]
        moving = ring(radius=7 if n == 'bronze_flywheel' else 6, inner=4, depth=3, texture='wood')
        moving += [box((2, 7, 6), (14, 9, 9)), box((7, 2, 6), (9, 14, 9)), box((5.5, 5.5, 4), (10.5, 10.5, 12), 'metal')]
        if n == 'bronze_hand_crank':
            moving = [box((6, 6, 5), (10, 10, 12), 'metal'), box((7, 7, 4), (9, 14, 6), 'metal'), box((7, 12, 0), (10, 15, 5))]
    elif n == 'bronze_cam_block':
        fixed = frame()
        moving = [box((6, 6, 0), (10, 10, 16), 'metal')] + ring(radius=4, inner=0, depth=4, center=(9.5, 8, 8))
        entry['animation'] = 'rotate_z'
    elif n == 'bronze_spindle_block':
        fixed = frame()
        moving = [box((6, 0, 6), (10, 16, 10), 'metal'), box((3, 5, 5), (12, 10, 12), 'metal')]
        entry['animation'] = 'rotate_y'
    elif n in ('bronze_work_table', 'bronze_template_holder', 'wooden_tool_clamp', 'bronze_tool_clamp'):
        fixed = [box((1, 11, 1), (15, 15, 15))]
        fixed += [box((x, 0, z), (x + 3, 11, z + 3)) for x in (2, 11) for z in (2, 11)]
        if n == 'bronze_template_holder':
            fixed += [box((2, 15, 5), (14, 16, 14), 'ceramic')]
        elif 'clamp' in n:
            t = 'metal' if n.startswith('bronze') else 'wood'
            fixed += [box((2, 15, 4), (5, 16, 12), t), box((11, 15, 4), (14, 16, 12), t)]
    elif n == 'bronze_hammer_arm':
        fixed = [box((1, 1, 6), (4, 14, 10)), box((0, 10, 4), (5, 13, 12), 'metal')]
        moving = [box((2, 10, 6), (15, 13, 10)), box((11, 3, 4), (15, 10, 12), 'stone')]
        entry['animation'] = 'rock_z'
    elif n in ('bronze_stone_mortar', 'bronze_grinding_bed', 'bronze_casting_socket', 'forge_work_bed'):
        t = 'ceramic' if n == 'forge_work_bed' else 'stone' if n != 'bronze_casting_socket' else 'wood'
        fixed = [box((1, 0, 1), (15, 3, 15), t), box((1, 3, 1), (4, 10, 15), t), box((12, 3, 1), (15, 10, 15), t), box((4, 3, 1), (12, 10, 4), t), box((4, 3, 12), (12, 10, 15), t)]
    elif n in ('bronze_grinding_runner', 'bronze_casting_rotor', 'bronze_capstan_post'):
        fixed = [box((2, 0, 2), (14, 3, 14), 'stone')]
        if n == 'bronze_capstan_post':
            moving = [box((5, 3, 5), (11, 16, 11)), box((4, 12, 4), (12, 15, 12), 'metal')]
        else:
            moving = ring('y', radius=7, inner=0, depth=4, texture='stone' if 'grinding' in n else 'wood', center=(8, 7, 8)) + [box((6, 3, 6), (10, 13, 10), 'metal')]
    elif n == 'bronze_pump_cylinder':
        fixed = [box((3, 0, 3), (13, 3, 13), 'metal'), box((4, 3, 4), (12, 12, 12), 'metal'), box((3, 12, 3), (13, 14, 13), 'dark')]
        moving = [box((6, 9, 6), (10, 16, 10), 'metal')]
    elif n in ('bronze_pump_inlet', 'bronze_pump_outlet'):
        fixed = [box((5, 5, 0), (11, 11, 16), 'metal'), box((3, 3, 5), (13, 13, 11), 'metal'), box((6, 12, 3), (10, 14, 10), 'dark')]
        fixed += [box((5, 13, 2 if n.endswith('inlet') else 11), (11, 15, 5 if n.endswith('inlet') else 14), 'metal')]
    elif n in ('bronze_bucket_section', 'bronze_bucket_top', 'bronze_bucket_bottom'):
        fixed = [box((1, 0, 1), (4, 16, 15)), box((12, 0, 1), (15, 16, 15)), box((4, 0, 12), (12, 16, 15))]
        moving = [box((6, 0, 10), (10, 16, 12), 'rope'), box((4, 4, 4), (12, 7, 11), 'metal'), box((4, 7, 9), (12, 10, 11), 'metal')]
        if n != 'bronze_bucket_section':
            moving = ring('x', radius=5, inner=2.5, depth=5, texture='wood') + [box((1, 6, 6), (15, 10, 10), 'metal')]
            entry['animation'] = 'rotate_x'
    elif n == 'bronze_hoist_drum':
        fixed = [box((0, 0, 5), (3, 15, 11)), box((13, 0, 5), (16, 15, 11))]
        moving = [box((3, 4, 4), (13, 12, 12), 'rope'), box((1, 2, 2), (4, 14, 14), 'metal'), box((12, 2, 2), (15, 14, 14), 'metal')]
        entry['animation'] = 'rotate_x'
    elif n in ('bronze_hoist_guide', 'bronze_hoist_landing'):
        fixed = [box((5, 0, 5), (11, 16, 11)), box((3, 1, 3), (13, 3, 13), 'metal'), box((3, 13, 3), (13, 15, 13), 'metal')]
        if n.endswith('landing'):
            fixed += [box((1, 0, 0), (15, 4, 5), 'stone')]
    elif n == 'bronze_hoist_platform':
        fixed = [box((0, 0, 0), (16, 3, 16)), box((0, 3, 0), (2, 5, 16), 'metal'), box((0, 3, 0), (16, 5, 2), 'metal')]
    elif n == 'bronze_hoist_counterweight':
        fixed = [box((4, 1, 4), (12, 13, 12), 'stone'), box((3, 3, 3), (13, 5, 13), 'metal'), box((7, 13, 7), (9, 16, 9), 'rope')]
    elif n == 'bronze_capstan_arm':
        fixed = [box((0, 7, 5), (16, 11, 11)), box((16, 7, 5), (32, 11, 11)), box((1, 6, 4), (4, 12, 12), 'metal')]
    elif n == 'bloomery_work_door':
        fixed = [box((0, 0, 5), (16, 16, 16), 'ceramic'), box((3, 2, 3), (13, 14, 5), 'dark'), box((10, 7, 1), (12, 10, 3), 'metal')]
    elif n == 'forge_stone_anvil':
        fixed = [box((2, 0, 2), (14, 3, 14), 'stone'), box((5, 3, 4), (11, 10, 12), 'stone'), box((0, 10, 1), (16, 15, 15), 'stone')]
    elif n == 'bronze_machine_item_port':
        fixed = [box((0, 0, 0), (16, 3, 16)), box((0, 3, 0), (3, 16, 16)), box((13, 3, 0), (16, 16, 16)), box((3, 13, 0), (13, 16, 16)), box((3, 3, 11), (13, 13, 15), 'dark')]
    elif entry['group'] == 'controller':
        # Open console with a substantial wooden body, metal dial and visible lever.
        fixed = [box((1, 0, 3), (15, 3, 15)), box((2, 3, 4), (14, 11, 14)), box((1, 11, 2), (15, 13, 15), 'metal'), box((3, 13, 4), (8, 15, 9), 'dark'), box((10, 13, 10), (12, 16, 12))]
        # A machine-specific silhouette is readable even in an item icon.
        if 'mill' in n:
            fixed += [box((4, 13, 9), (9, 16, 14), 'stone')]
        elif 'forge' in n or 'bloomery' in n:
            fixed[1] = box((2, 3, 4), (14, 11, 14), 'ceramic')
        elif 'pump' in n:
            fixed += [box((10, 4, 0), (14, 8, 4), 'metal')]
        elif 'hoist' in n or 'elevator' in n or 'capstan' in n:
            fixed += [box((0, 4, 8), (3, 11, 12), 'rope')]
    elif n == 'bronze_batch_gate':
        fixed = [box((1, 1, 1), (4, 15, 15)), box((12, 1, 1), (15, 15, 15)), box((4, 5, 2), (12, 8, 14), 'metal'), box((1, 12, 1), (15, 15, 4))]
    elif n in ('bronze_beam', 'workshop_wheel_spoke'):
        fixed = [box((5, 5, 0), (11, 11, 16)), box((4, 4, 1), (12, 12, 3), 'metal')]
    elif n in ('bronze_corner_brace', 'bronze_cross_brace'):
        # Quarter-turn voxel steps keep collision and pixels aligned.
        for i in range(7):
            fixed += [box((i * 2, i * 2, 6), (min(16, i * 2 + 4), min(16, i * 2 + 4), 10))]
            if n.endswith('cross_brace'):
                fixed += [box((i * 2, 12 - i * 2, 6), (min(16, i * 2 + 4), 16 - i * 2, 10))]
    elif n in ('bronze_bearing_cover', 'bronze_pulley_cap', 'bronze_gear_plaque', 'bronze_inspection_cover'):
        fixed = [box((3, 3, 13), (13, 13, 16), 'metal'), box((6, 6, 11), (10, 10, 13), 'dark')]
        if n == 'bronze_gear_plaque':
            fixed += ring(radius=6, inner=3, depth=1, center=(8, 8, 12))
    elif n in ('bronze_overhead_bracket', 'bronze_floor_bracket', 'workshop_cable_saddle', 'workshop_hanging_hook'):
        fixed = [box((2, 0, 10), (14, 3, 16)), box((5, 3, 11), (11, 14, 15)), box((5, 11, 4), (11, 14, 11), 'metal')]
        if n == 'workshop_hanging_hook':
            fixed = [box((7, 9, 7), (9, 16, 9), 'rope'), box((6, 2, 7), (8, 10, 9), 'metal'), box((8, 1, 7), (12, 3, 9), 'metal'), box((11, 3, 7), (13, 6, 9), 'metal')]
    elif n == 'bronze_machine_cornice':
        fixed = [box((0, 10, 8), (16, 16, 16)), box((0, 9, 7), (16, 11, 16), 'metal')]
    elif n == 'bronze_shaft_guard':
        fixed = [box((0, 0, 0), (2, 12, 16), 'metal'), box((14, 0, 0), (16, 12, 16), 'metal'), box((2, 11, 0), (14, 13, 2), 'metal'), box((2, 11, 7), (14, 13, 9), 'metal'), box((2, 11, 14), (14, 13, 16), 'metal')]
    elif n == 'workshop_drive_pedestal':
        fixed = [box((1, 0, 1), (15, 8, 15), 'stone'), box((0, 8, 0), (16, 11, 16))]
    elif n == 'workshop_loading_chute':
        fixed = [box((1, 0, 0), (3, 10, 16)), box((13, 0, 0), (15, 10, 16))]
        for i in range(4):
            fixed += [box((3, i * 2, i * 4), (13, i * 2 + 2, i * 4 + 4))]
    else:
        raise ValueError('Missing authored geometry: ' + n)
    return fixed, moving


def textures():
    palettes = {
        'wood': ['49301e', '654224', '855b31', 'a77942', 'c79756'],
        'metal': ['59401f', '80602d', 'a67d39', 'c99c4d', 'e8c379'],
        'dark': ['252729', '363a3c', '4b5050', '626765', '7b8077'],
        'stone': ['555348', '716e60', '8c8877', 'a8a28f', 'c2baa3'],
        'rope': ['3f3021', '5a4229', '795c38', '9f7e4c', 'c19c63'],
        'ceramic': ['4a352b', '6c4d39', '8b6649', 'ad855d', 'cfa879'],
    }
    for name, colors in palettes.items():
        colors = [tuple(bytes.fromhex(c)) for c in colors]
        im = Image.new('RGB', (16, 16))
        for y in range(16):
            for x in range(16):
                # Controlled broad grain; no unseeded noise or muddy dithering.
                level = 2 + (1 if (x + (y // 5)) % 7 == 1 else 0) - (1 if x % 8 == 0 else 0)
                if name == 'metal':
                    level = 4 if y == 0 else 0 if y == 15 else 2 + (x // 5) % 2
                elif name == 'stone':
                    level = 1 if y in (0, 8) else 2 + int((x // 4 + y // 4) % 4 == 0)
                elif name == 'rope':
                    level = 1 if (x + y // 2) % 5 == 0 else 3
                im.putpixel((x, y), colors[level])
        path = ASSETS / 'textures/block' / ('bronze_workshop_' + name + '.png')
        path.parent.mkdir(parents=True, exist_ok=True)
        im.save(path)


def icon(entry):
    n = entry['id']; im = Image.new('RGBA', (16, 16)); d = ImageDraw.Draw(im)
    c = ['#59401f', '#a67d39', '#c99c4d', '#e8c379']
    if 'pattern' in n:
        c = ['#555348', '#8c8877', '#a8a28f', '#c2baa3']
    if 'mold' in n:
        sand = n.startswith('sand'); c = ['#6b5636', '#bda373', '#d8bf8b', '#eddaac'] if sand else ['#7c5823', '#c39739', '#e6bd53', '#ffe693']
        d.rectangle((1, 2, 14, 13), fill=c[0]); d.rectangle((2, 2, 13, 12), fill=c[1]);d.line((2, 2, 13, 2), fill=c[3]);d.ellipse((5, 5, 10, 10), fill=c[0]);d.line((7, 3, 7, 6), fill=c[0])
    elif any(k in n for k in ('gear', 'cam', 'hub', 'bushing', 'die', 'piston')):
        d.ellipse((3, 3, 12, 12), fill=c[0]);d.ellipse((3, 2, 12, 11), fill=c[1]);d.arc((3, 2, 12, 11), 180, 300, fill=c[3]);d.rectangle((6, 5, 9, 8), fill=c[0])
        if 'gear' in n:
            for a in range(0, 360, 45):
                x, y = round(7.5 + 6 * math.cos(math.radians(a))), round(7 + 6 * math.sin(math.radians(a)));d.rectangle((x - 1, y - 1, x + 1, y + 1), fill=c[2])
        elif 'piston' in n:
            d.rectangle((6, 9, 9, 14), fill=c[1]);d.rectangle((3, 2, 12, 5), fill=c[2])
        elif 'die' in n:
            d.rectangle((2, 3, 13, 12), fill=c[1]);d.rectangle((5, 6, 10, 9), fill=c[0])
        if n.startswith('cast_'):
            d.rectangle((6, 11, 8, 14), fill=c[0]);d.rectangle((4, 14, 10, 15), fill=c[1])
    elif any(k in n for k in ('belt', 'harness', 'seal', 'hitch')):
        d.ellipse((2, 2, 13, 14), outline='#49301e', width=3);d.arc((3, 2, 13, 13), 190, 300, fill='#b48750', width=1);d.rectangle((5, 1, 9, 4), outline=c[3], width=1)
    elif 'template' in n:
        d.rectangle((2, 2, 13, 13), fill='#654224');d.rectangle((3, 2, 12, 12), fill='#c79756');d.ellipse((5, 4, 10, 9), outline='#49301e', width=2)
    elif 'drill' in n:
        d.line((3, 14, 12, 3), fill=c[0], width=3);d.line((4, 13, 13, 2), fill=c[2], width=2)
        for i in range(4):d.line((5 + i * 2, 13 - i * 2, 3 + i * 2, 11 - i * 2), fill=c[0])
    elif any(k in n for k in ('grog', 'abrasive', 'mix')):
        for a, b in [((2, 9), (6, 12)), ((7, 6), (12, 10)), ((5, 12), (12, 14)), ((10, 3), (13, 5))]:d.rectangle((*a, *b), fill='#b6a084' if 'abrasive' not in n else '#8e9291');d.point(a, fill='#e1cdb0')
    elif any(k in n for k in ('iron', 'bloom')):
        d.polygon([(1, 11), (4, 5), (11, 3), (14, 7), (12, 12), (4, 14)], fill='#55595b');d.line((4, 5, 11, 3, 14, 7), fill='#d1cfbd', width=2);d.line((4, 12, 11, 10), fill='#959997', width=2)
    else:
        d.ellipse((2, 3, 13, 13), fill='#49301e');d.ellipse((3, 2, 12, 10), fill='#a77942');d.arc((3, 2, 12, 10), 180, 320, fill='#e0b97a');d.rectangle((6, 5, 9, 8), fill='#654224')
    path = ASSETS / 'textures/item' / (n + '.png');path.parent.mkdir(parents=True, exist_ok=True);im.save(path)


def main():
    catalog = json.loads(CATALOG.read_text(encoding='utf-8'))
    textures()
    for b in catalog['blocks']:
        n = b['id']; fixed, moving = geometry(b)
        textures_map = {t: 'smog:block/bronze_workshop_' + t for t in ('wood', 'metal', 'dark', 'stone', 'rope', 'ceramic')}
        textures_map['particle'] = textures_map['wood']
        base = {'parent': 'minecraft:block/block', 'textures': textures_map, 'elements': exterior(fixed)}
        write(ASSETS / 'models/block' / (n + '.json'), base)
        write(ASSETS / 'models/block' / (n + '_inventory.json'), {**base, 'elements': exterior(fixed + moving), 'smog_collision': [{'from': e['from'], 'to': e['to']} for e in fixed + moving]})
        # Physical cuboids are metadata on a valid model, not fake unbakeable models.
        old_collision = ASSETS / 'models/block' / (n + '_collision.json')
        if old_collision.exists():old_collision.unlink()
        # Every moving sub-model is selected through the existing block item; no hidden registrations.
        if moving:
            write(ASSETS / 'models/block' / (n + '_moving.json'), {**base, 'elements': exterior(moving)})
        cases = [{'when': 'moving', 'model': {'type': 'minecraft:model', 'model': 'smog:block/' + n + '_moving'}}] if moving else []
        item = {'type': 'minecraft:model', 'model': 'smog:block/' + n + '_inventory'}
        if cases:item = {'type': 'minecraft:select', 'property': 'minecraft:custom_model_data', 'index': 0, 'cases': cases, 'fallback': item}
        write(ASSETS / 'items' / (n + '.json'), {'model': item})
        variants = {}
        for face, angle in [('north', 0), ('east', 90), ('south', 180), ('west', 270), ('up', 0), ('down', 0)]:
            model = {'model': 'smog:block/' + n}
            if angle:model['y'] = angle
            if face == 'up':model['x'] = 90
            if face == 'down':model['x'] = 270
            variants['facing=' + face + ',assembled=false'] = model
            variants['facing=' + face + ',assembled=true'] = {**model, 'model': 'smog:block/' + n + '_inventory'}
        write(ASSETS / 'blockstates' / (n + '.json'), {'variants': variants})
        write(DATA / 'loot_table/blocks' / (n + '.json'), {'type': 'minecraft:block', 'pools': [{'rolls': 1, 'entries': [{'type': 'minecraft:item', 'name': 'smog:' + n}], 'conditions': [{'condition': 'minecraft:survives_explosion'}]}]})
    for i in catalog['items']:
        icon(i)
        write(ASSETS / 'models/item' / (i['id'] + '.json'), {'parent': 'minecraft:item/generated', 'textures': {'layer0': 'smog:item/' + i['id']}})
        write(ASSETS / 'items' / (i['id'] + '.json'), {'model': {'type': 'minecraft:model', 'model': 'smog:item/' + i['id']}})
    # A separate tag is included from the shared vanilla mining tag by the build merger.
    write(DATA / 'tags/block/bronze_workshop_blocks.json', {'replace': False, 'values': ['smog:' + b['id'] for b in catalog['blocks']]})
    write(CATALOG, catalog)
    print(f"Bronze assets: {len(catalog['blocks'])} blocks, {len(catalog['items'])} items; existing art unchanged")


if __name__ == '__main__':
    main()
