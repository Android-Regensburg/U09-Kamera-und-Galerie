# U09 | Kamera und Galerie

![Cover für die neunte Übungsaufgabe](./docs/cover.png)

## Downloads

- [Download des Starterpakets](https://github.com/Android-Regensburg/U09-Kamera-und-Galerie/archive/master.zip)
- [Download des Lösungsvorschlag](https://github.com/Android-Regensburg/U09-Kamera-und-Galerie/archive/solution.zip)

## Aufgabe

Im Rahmen dieses Übungsblattes ist eine App (`ImageTaker`) zu entwickeln, mit der sie Bilder machen können. Als Quelle kann der Nutzer ein Bild mit der Kamera des Gerätes schießen oder ein auf dem Gerät gespeichertes auswählen und auf dem Screen darstellen. In beiden Fällen sollte diese Funktionalität nicht selbst implementiert werden. Stattdessen wird die (jeweils auf dem System installierte) Kamera- bzw. Galerie-App von Android ein Bild an die eigene App liefern. (Fast) Jedes Android-Gerät hat eine oder mehrere Apps für diese Zwecke bereits vorinstalliert.

![Screenshot der ImageTaker-App](./docs/screenshot-1.png "Startbildschirm der App")

## Hinweise

* Verwenden sie die Vorlesung (vor allem VL8) sowie die Android Dokumentation, um die Features zu implementieren (https://developer.android.com/training/camera/photobasics).
* Versuchen sie auch weitere Quellen aus dem Internet hinzuzuziehen (z.B. Stackoverflow), um sich über die Funktionalitäten zu informieren und Probleme zu lösen.
* Vergessen sie nicht die notwendigen Berechtigungen im Android Manifest anzugeben.
* Versuchen sie Schritt für Schritt die Teilprobleme zu lösen und in einzelne Methoden auszulagern. Fangen sie damit an zuerst ein Thumbnail ihres geschossenen Fotos darzustellen und erweitern sie Schritt für Schritt die zu implementierenden Features.

## Fehlerbehebung

Die Vielfalt der Gerätelandschaft von Android bedingt, dass es immer wieder zu unerwarteten, hardwarebedingten Engpässen kommen kann. Als Programmierer sind diese einzukalkulieren. Zur Vereinfachung dieser Aufgabe wurde ein mögliches Problem, das beim Erzeugen der Bitmaps auftreten kann, absichtlich ignoriert.
Sollte Ihre App beim Dekodieren der Bilder in Bitmaps via `BitmapFactory` mit Hinweis auf eine Speicherüberschreitung abstürzen, kann dieses nicht in Originalgröße (Fotos können einige MB groß werden) geladen und dann verkleinert gerendert werden. Beim Umgang mit Bildern im UI kann dieses Problem in verschiedenen Formen zutage treten. Einen guten Überblick über best practices und mögliche Fallstricke bieten diese Quellen von Google:
* Überblick: http://developer.android.com/training/displaying-bitmaps/index.html
* Lösungsansatz für obiges Problem: http://developer.android.com/training/displaying-bitmaps/load-bitmap.html  

## Anhang
### Screenshots
![Screenshot der Kamera-App](./docs/screenshot-2.png "Kameraansicht")