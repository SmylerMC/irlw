#!/usr/bin/env python3

from PIL import Image
from sys import argv

def remove_vertical_line(img, x):
	for y in range(img.height):
		p = img.getpixel((x, y))
		if p != (0, 255, 255):
			continue
		cdic = {}
		for dx in range(-1, 2):
			for dy in range(-1, 2):
				try:
					p = img.getpixel((x+dx, y+dy))
				except IndexError:
					continue
				if p in cdic:
					cdic[p] += 1
				else:
					cdic[p] = 0
		color = 0, 0, 0
		score = 0
		for key, item in cdic.items():
			if item > score and key != (0, 255, 255):
				color = key

		img.putpixel((x, y), color)

def remove_horizontal_line(img, y):
	for x in range(img.width):
		p = img.getpixel((x, y))
		if p != (0, 255, 255):
			continue
		cdic = {}
		for dx in range(-1, 2):
			for dy in range(-1, 2):
				try:
					p = img.getpixel((x+dx, y+dy))
				except IndexError:
					continue
				if p in cdic:
					cdic[p] += 1
				else:
					cdic[p] = 0
		color = 0, 0, 0
		score = 0
		for key, item in cdic.items():
			if item > score and key != (0, 255, 255):
				color = key

		img.putpixel((x, y), color)

def remove_black_lines(img):

	for x in range(img.width):
		for y in range(img.height):
			p = img.getpixel((x, y))
			if p != (0, 0, 0):
				continue
			cdic = {}
			for dx in range(-1, 2):
				for dy in range(-1, 2):
					try:
						p = img.getpixel((x+dx, y+dy))
					except IndexError:
						continue
					if p in cdic:
						cdic[p] += 1
					else:
						cdic[p] = 0
			color = 0, 255, 255
			score = 0
			for key, item in cdic.items():
				if item > score and key != (0, 0, 0):
					color = key

			img.putpixel((x, y), color)


try:
	infname = argv[1]
	outfname = argv[2]
except:
	print("Usage: {} <infile> <outfile>".format(argv[0]))
	exit(-1)

try:
	img = Image.open(infname)
except:
	print("Failed to read image!")
	exit(-2)

if img.size != (6800, 4400):
	print("Image is not of the correct size")
	exit(-3)
	
img = img.crop(box=(237, 454, 6547, 4132))

m = img.crop(box=(2700, 2700, 2710, 2710))
img.paste(m.resize((1300, 2060)), box=(0, 1629))
img.paste(m.resize((1435, 214)), box=(4331, 3189))
img.paste(m.resize((206, 25)), box=(4936, 3405))

for x in (525, 1051, 1577, 2103, 2629, 3155, 3681, 4207, 4733, 5259, 5785, ):
	remove_vertical_line(img, x)

for y in (873, 1534, 2075, 2617, 3278):
	remove_horizontal_line(img, y)

remove_black_lines(img)

img.save(outfname)
