"""Generates the downloadable CV PDFs (EN/DE) from the site's i18n files.

Usage (needs `pip install reportlab`), from src/main/app:
    python scripts/generate-cv.py

Texts come from src/assets/i18n/resume-<lang>.json, so they stay in sync with the
website. Dates, companies and skills are language-neutral and listed below:
update ENTRIES when a job is added. No phone number or street address is printed.
"""
import json
from pathlib import Path

from reportlab.lib import colors
from reportlab.lib.enums import TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import HRFlowable, KeepTogether, Paragraph, SimpleDocTemplate, Spacer

ROOT = Path(__file__).resolve().parent.parent
I18N = ROOT / "src" / "assets" / "i18n"
OUT = ROOT / "src" / "assets" / "cv"

NAME = "Wael Chabchoub"
EMAIL = "waelchabchoub@outlook.com"

# (title key, description key, dates, company, skills) - newest first
PE = "PROFFESIONALEXPERIENCE"
ENTRIES = [
    ("TITLE7", "DESCRIPTION7", "03.2026 - Present", "LIMAA Technologies, Cologne, DE",
     "Java · Spring Boot · Python · TypeScript · React · PostgreSQL · RabbitMQ · Docker · GitHub Actions · IEC 62304 · Penetration Testing"),
    ("TITLE6", "DESCRIPTION6", "03.2023 - 02.2026", "Invitec GmbH & Co. KG, Aachen, DE",
     "Aurelia · Spring Boot · Jenkins · Docker · Asciidoctor · Java · TypeScript"),
    ("TITLE5", "DESCRIPTION5", "06.2021 - 02.2023", "GOMYCODE, Sousse, TN",
     "Penetration Testing · Scripting · Intrusion Detection · Intrusion Prevention · Malware Analysis"),
    ("TITLE4", "DESCRIPTION4", "12.2020 - 02.2023", "GOMYCODE, Sousse, TN",
     "Supervised and Unsupervised algorithms · Data Preprocessing · Features Extraction · Classification · Regression · Clustering · NLP"),
    ("TITLE3", "DESCRIPTION3", "06.2022 - 07.2022", "ETC Tunisie, Remote",
     "HTML5 · CSS3 · JSON Web Token (JWT) · Angular · Symfony · REST APIs · API Platform · TypeScript · PHP · Bootstrap"),
    ("TITLE2", "DESCRIPTION2", "08.2021 - 09.2021", "Gpro consulting, Sousse, TN",
     "Flutter · Flutter Mobile Vision"),
    ("TITLE1", "DESCRIPTION1", "07.2021 - 08.2021", "Inside Conseils, Sousse, TN",
     "Python · Odoo"),
]
EDUCATION = [("DEGREE2", "2020 - 2023", "UNIVERSITY2", "DESCRIPTION2"),
             ("DEGREE1", "2017 - 2020", "UNIVERSITY1", "DESCRIPTION1")]
SKILLS_LABEL = {"en": "Skills", "de": "Kenntnisse"}

ACCENT = colors.HexColor("#1f3a5f")
GREY = colors.HexColor("#555555")
S = {
    "name": ParagraphStyle("name", fontName="Helvetica-Bold", fontSize=22, textColor=ACCENT, leading=26),
    "contact": ParagraphStyle("contact", fontName="Helvetica", fontSize=10, textColor=GREY, leading=14),
    "h2": ParagraphStyle("h2", fontName="Helvetica-Bold", fontSize=12, textColor=ACCENT, spaceBefore=10, spaceAfter=3,
                         leading=15),
    "h3": ParagraphStyle("h3", fontName="Helvetica-Bold", fontSize=10.5, leading=13),
    "meta": ParagraphStyle("meta", fontName="Helvetica-Oblique", fontSize=9, textColor=GREY, leading=12),
    "body": ParagraphStyle("body", fontName="Helvetica", fontSize=9.5, leading=13, alignment=TA_LEFT),
}


def esc(text: str) -> str:
    return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")


def section(story, title):
    story.append(Paragraph(esc(title), S["h2"]))
    story.append(HRFlowable(width="100%", thickness=0.6, color=ACCENT, spaceAfter=4))


def build(lang: str) -> Path:
    t = json.loads((I18N / f"resume-{lang}.json").read_text(encoding="utf-8"))["RESUME"]
    OUT.mkdir(parents=True, exist_ok=True)
    path = OUT / f"Wael_Chabchoub_CV_{lang}.pdf"
    doc = SimpleDocTemplate(str(path), pagesize=A4, leftMargin=18 * mm, rightMargin=18 * mm, topMargin=16 * mm,
                            bottomMargin=16 * mm, title=f"{NAME} - CV", author=NAME)
    story = [Paragraph(NAME, S["name"]), Paragraph(EMAIL, S["contact"]), Spacer(1, 4)]

    section(story, t["SUMMARY"]["SELF"])
    story.append(Paragraph(esc(t["SUMMARY"]["DESCRIPTION"]), S["body"]))

    section(story, t[PE]["SELF"])
    for title_key, desc_key, dates, company, skills in ENTRIES:
        story.append(KeepTogether([
            Paragraph(esc(t[PE][title_key]), S["h3"]),
            Paragraph(f"{esc(dates)} · {esc(company)}", S["meta"]),
            Spacer(1, 2),
            Paragraph(esc(t[PE][desc_key]), S["body"]),
            Paragraph(f"<b>{SKILLS_LABEL[lang]}:</b> {esc(skills)}", S["body"]),
            Spacer(1, 7),
        ]))

    section(story, t["EDUCATION"]["SELF"])
    for degree, dates, university, desc in EDUCATION:
        story.append(KeepTogether([
            Paragraph(esc(t["EDUCATION"][degree]), S["h3"]),
            Paragraph(f"{dates} · {esc(t['EDUCATION'][university])}", S["meta"]),
            Spacer(1, 2),
            Paragraph(esc(t["EDUCATION"][desc]), S["body"]),
            Spacer(1, 7),
        ]))

    doc.build(story)
    return path


if __name__ == "__main__":
    for language in ("en", "de"):
        print("written", build(language))
