/* Request - Holds all information needed for a (partial) piece request.
   Copyright (C) 2003 Mark J. Wielaard
   This file is part of Snark.
   Licensed under the GPL version 2 or later.
*/

package org.klomp.snark;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Holds all information needed for a partial piece request.
 * This class should be used only by PeerState, PeerConnectionIn, and PeerConnectionOut.
 */
class Request
{
  private final PartialPiece piece;
  final int off;
  final int len;
  long sendTime;

  /**
   * Creates a new Request.
   *
   * @param piece Piece number requested.
   * @param off the offset in the array.
   * @param len the number of bytes requested.
   */
  Request(PartialPiece piece, int off, int len)
  {
    this.piece = piece;
    this.off = off;
    this.len = len;

    // Sanity check
    if (off < 0 || len <= 0 || off + len > piece.getLength())
      throw new IndexOutOfBoundsException("Illegal Request " + toString());
  }

  /**
   * Dummy Request for PeerState.returnPartialPieces().
   * len will be zero.
   *
   * @param piece Piece number requested.
   * @param off the offset in the array.
   * @since 0.9.36
   */
  Request(PartialPiece piece, int off)
  {
    this.piece = piece;
    this.off = off;
    this.len = 0;

    // Sanity check
    if (off < 0 || off > piece.getLength())
      throw new IndexOutOfBoundsException("Illegal Request " + toString());
  }

  /**
   *  @since 0.9.1
   */
  public void read(DataInputStream din, BandwidthListener bwl) throws IOException {
      piece.read(din, off, len, bwl);
  }

  /**
   *  The piece number this Request is for
   *
   *  @since 0.9.1
   */
  public int getPiece() {
      return piece.getPiece();
  }

  /**
   *  The PartialPiece this Request is for
   *
   *  @since 0.9.1
   */
  public PartialPiece getPartialPiece() {
      return piece;
  }

    @Override
  public int hashCode()
  {
    return piece.getPiece() ^ off ^ len;
  }

    @Override
  public boolean equals(Object o)
  {
    if (o instanceof Request)
      {
        Request req = (Request)o;
        return req.piece.equals(piece) && req.off == off && req.len == len;
      }

    return false;
  }

    @Override
  public String toString()
  {
    return "(" + piece.getPiece() + "," + off + "," + len + ")";
  }
}
