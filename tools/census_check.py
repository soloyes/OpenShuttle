"""Compares what was actually in each sector (CENSUS lines) with the sector's own table (CAPS)."""
import glob, re, sys, os
S = os.path.dirname(os.path.abspath(__file__))

def kv(line):
    return dict(re.findall(r"(\w+)=([^\s]+)", line))

total_bad = 0
for log in sorted(glob.glob(f"{S}/census/s*.log"), key=lambda p: int(re.findall(r"s(\d+)\.log", p)[0])):
    lines = open(log).read().splitlines()
    caps = [kv(l) for l in lines if "CAPS sector=" in l]
    samples = [kv(l) for l in lines if "CENSUS sector=" in l]
    n = re.findall(r"s(\d+)\.log", log)[0]
    if not caps or not samples:
        print(f"sector {n}: NO DATA ({len(caps)} caps, {len(samples)} samples)")
        total_bad += 1
        continue
    c = caps[-1]
    samples = [x for x in samples if x.get("sector") == c["sector"]]
    I = lambda d, k: int(d[k])
    esc = c["escalate"] == "true"
    limits = {
        # Bosses: the Mother (06) and her brood of up to three, the Storm Heart (12).
        "corrosive": I(c, "corrosive") + (10 if esc else 0) + (6 if c["sector"] == "6" else 0),
        "electric": max(I(c, "electric"), 3 if esc else 0) + (1 if c["sector"] in ("2", "12") else 0),
        "radiation": I(c, "radiation"),
        "magnetic": I(c, "magnetic"),
        "rocks": ((I(c, "rocks") + 1) // 2) * 2,
        "crew": I(c, "crew") + (1 if c["sector"] == "16" else 0),   # 16: the choice
        "crates": I(c, "crates") + 1,   # the hidden cache
        "storages": I(c, "storages") + (1 if c["sector"] == "2" else 0),
        "fuel": I(c, "fuel") + (1 if c["sector"] == "16" else 0),
        "volleys": 0,
        "step": 0,
    }
    bad = []
    mx = {k: max(int(s[k]) for s in samples) for k in limits}
    for k, lim in limits.items():
        if mx[k] > lim:
            bad.append(f"{k} {mx[k]} > {lim}")
    if I(c, "rocks") == 0 and max(int(s["fragments"]) for s in samples) > 0:
        bad.append("fragments without rocks")
    if any(s["hole"] != c["hole"] for s in samples):
        bad.append(f"hole {set(s['hole'] for s in samples)} vs {c['hole']}")
    if any(int(s["shelters"]) != I(c, "shelters") for s in samples):
        bad.append(f"shelters {set(s['shelters'] for s in samples)} vs {c['shelters']}")
    if c["noradar"] == "true" and any(s["radar"] == "true" for s in samples):
        bad.append("radar on in a no-radar sector")
    if any(s["death"] == "true" for s in samples):
        bad.append("DEATH MODE")
    phases = sorted(set(s["phase"] for s in samples))
    seen = ", ".join(f"{k}={mx[k]}" for k in limits if mx[k] > 0)
    status = "OK " if not bad else "BAD"
    total_bad += bool(bad)
    print(f"sector {n:>2} {status} {len(samples):>3} samples  phases={phases}  seen: {seen}")
    for b in bad:
        print("        !", b)
print("violations in", total_bad, "sectors")
