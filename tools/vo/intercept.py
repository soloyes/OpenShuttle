"""Turns dry voice takes into far-away Earth broadcasts (the 1991 intercepts between sectors):
a narrow AM band, a fading signal, crackle and a hiss bed, unlike the clean mission-control
radio. Reads <id>_dry.wav, writes assets/sound/radio/<id>.mp3.

Usage: python3 tools/vo/intercept.py <dir with earthNN_dry.wav>
"""
import glob
import os
import subprocess
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from render_radio import OUT  # noqa: E402

CHAIN = (
    "[0:a]aresample=44100,highpass=f=450,lowpass=f=2600,"
    "acompressor=threshold=-24dB:ratio=8:attack=3:release=120:makeup=5,"
    "asoftclip=type=tanh:threshold=0.5,tremolo=f=0.6:d=0.35,"
    "adelay=300|300,apad=pad_dur=0.6[v];"
    "anoisesrc=color=white:amplitude=0.06:sample_rate=44100,highpass=f=900,lowpass=f=5000,tremolo=f=9:d=0.7[n];"
    "anoisesrc=color=brown:amplitude=0.05:sample_rate=44100,lowpass=f=600[h];"
    "[v][n][h]amix=inputs=3:duration=first:weights=1 0.6 0.5:normalize=0,"
    "afade=t=in:d=0.25,areverse,afade=t=in:d=0.4,areverse,alimiter=limit=0.9[out]"
)


def main():
    src = sys.argv[1]
    for dry in sorted(glob.glob(os.path.join(src, "earth*_dry.wav"))):
        name = os.path.basename(dry).replace("_dry.wav", "")
        out = os.path.join(OUT, name + ".mp3")
        subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", dry, "-filter_complex", CHAIN,
                        "-map", "[out]", "-ac", "1", "-c:a", "libmp3lame", "-q:a", "5", out], check=True)
        print(out)


if __name__ == "__main__":
    main()
